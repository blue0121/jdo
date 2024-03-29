package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.core.annotation.Must;
import io.jutil.jdo.core.annotation.Transient;
import io.jutil.jdo.core.annotation.Version;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.ColumnMetadata;
import io.jutil.jdo.core.parser.FieldMetadata;
import io.jutil.jdo.core.parser.IdMetadata;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.core.parser.MapperMetadata;
import io.jutil.jdo.core.parser.TransientMetadata;
import io.jutil.jdo.core.reflect.ClassFieldOperation;
import io.jutil.jdo.core.reflect.ReflectFactory;
import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.parser.model.DefaultColumnMetadata;
import io.jutil.jdo.internal.core.parser.model.DefaultEntityMetadata;
import io.jutil.jdo.internal.core.parser.model.DefaultIdMetadata;
import io.jutil.jdo.internal.core.parser.model.DefaultTransientMetadata;
import io.jutil.jdo.internal.core.parser.model.DefaultVersionMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-05-12
 */
public class EntityParser extends AbstractParser {
	private static Logger logger = LoggerFactory.getLogger(EntityParser.class);
	private static final Set<Class<?>> INT_TYPE_SET = Set.of(int.class, long.class, Integer.class, Long.class);

	public EntityParser(Dialect dialect) {
		super(dialect);
	}

    @Override
    public MapperMetadata parse(Class<?> clazz) {
		var classOperation = ReflectFactory.getClassOperation(clazz);
	    Entity annotationEntity = classOperation.getAnnotation(Entity.class);
	    String tableName = annotationEntity.table();
	    if (tableName.isEmpty()) {
		    tableName = this.toColumnName(classOperation.getName());
	    }

	    logger.info("实体类映射: {} <==> {}", classOperation.getTargetClass().getName(), tableName);
	    var entityMetadata = new DefaultEntityMetadata();
	    entityMetadata.setTableName(tableName);
	    entityMetadata.setClassOperation(classOperation);
	    Map<String, IdMetadata> idMap = new LinkedHashMap<>();
	    Map<String, ColumnMetadata> columnMap = new LinkedHashMap<>();
	    Map<String, TransientMetadata> transientMap = new LinkedHashMap<>();
		Map<String, FieldMetadata> fieldMap = new LinkedHashMap<>();

	    var fields = classOperation.getAllFields();
	    for (var entry : fields.entrySet()) {
		    var field = entry.getValue();
		    if (this.parseFieldId(field, idMap, fieldMap)) {
			    continue;
		    }
			if (this.parseFieldVersion(field, entityMetadata, fieldMap)) {
				continue;
			}
			if (this.parseFieldTransient(field, transientMap, fieldMap)) {
				continue;
			}

			this.parseFieldColumn(field, columnMap, fieldMap);
	    }

	    entityMetadata.setIdMap(idMap);
	    entityMetadata.setColumnMap(columnMap);
	    entityMetadata.setTransientMap(transientMap);
		entityMetadata.setFieldMap(fieldMap);

	    var generator = new DefaultSqlGenerator(entityMetadata);
	    generator.generate();

	    entityMetadata.check();
        return entityMetadata;
    }

	private boolean parseFieldId(ClassFieldOperation field, Map<String, IdMetadata> idMap,
	                             Map<String, FieldMetadata> fieldMap) {
		Id annotationId = field.getAnnotation(Id.class);
		if (annotationId == null) {
			return false;
		}

		var type = field.getType();
		var idType = switch (type.getSimpleName()) {
			case "String" -> IdType.STRING;
			case "int", "Integer" -> IdType.INT;
			case "long", "Long" -> IdType.LONG;
			default -> throw new UnsupportedOperationException("不支持主键类型: " + type.getSimpleName());
		};
		var generatorType = this.getAndCheckGeneratorType(annotationId, idType);

		var metadata = new DefaultIdMetadata();
		this.setFieldMetadata(field, metadata);
		metadata.setIdType(idType);
		metadata.setGeneratorType(generatorType);

		idMap.put(metadata.getFieldName(), metadata);
		fieldMap.put(metadata.getFieldName(), metadata);
		logger.debug("主键字段: {} <==> {}", metadata.getFieldName(), metadata.getColumnName());
		return true;
	}

	private GeneratorType getAndCheckGeneratorType(Id annotation, IdType type) {
		var generatorType = annotation.generator();
		if (generatorType == GeneratorType.AUTO) {
			generatorType = switch (type) {
				case STRING -> GeneratorType.UUID;
				default -> GeneratorType.INCREMENT;
			};
		}
		if (type == IdType.STRING && generatorType == GeneratorType.INCREMENT) {
			throw new IllegalArgumentException("主键类型 " + type + " 不支持 " + generatorType);
		}
		if (type == IdType.INT || type == IdType.LONG) {
			if (generatorType == GeneratorType.UUID) {
				throw new IllegalArgumentException("主键类型 " + type + " 不支持 " + generatorType);
			}
		}

		return generatorType;
	}

	private boolean parseFieldVersion(ClassFieldOperation field, DefaultEntityMetadata entityMetadata,
	                                  Map<String, FieldMetadata> fieldMap) {
		Version annotationVersion = field.getAnnotation(Version.class);
		if (annotationVersion == null) {
			return false;
		}

		var type = field.getType();
		if (!INT_TYPE_SET.contains(type)) {
			throw new JdbcException("@Version 类型必需是: " + INT_TYPE_SET);
		}

		var metadata = new DefaultVersionMetadata();
		this.setFieldMetadata(field, metadata);
		metadata.setForce(annotationVersion.force());
		metadata.setDefaultValue(annotationVersion.defaultValue());
		entityMetadata.setVersionMetadata(metadata);
		fieldMap.put(metadata.getFieldName(), metadata);
		logger.debug("版本字段: {} <==> {}", metadata.getFieldName(), metadata.getColumnName());
		return true;
	}

	private boolean parseFieldTransient(ClassFieldOperation field, Map<String, TransientMetadata> transientMap,
	                                 Map<String, FieldMetadata> fieldMap) {
		Transient annotationTransient = field.getAnnotation(Transient.class);
		if (annotationTransient == null) {
			return false;
		}

		var metadata = new DefaultTransientMetadata();
		this.setFieldMetadata(field, metadata);
		transientMap.put(metadata.getFieldName(), metadata);
		fieldMap.put(metadata.getFieldName(), metadata);
		logger.debug("额外字段: {} <==> {}", metadata.getFieldName(), metadata.getColumnName());
		return true;
	}

	private void parseFieldColumn(ClassFieldOperation field, Map<String, ColumnMetadata> columnMap,
	                              Map<String, FieldMetadata> fieldMap) {
		var metadata = new DefaultColumnMetadata();
		this.setFieldMetadata(field, metadata);

		Must annotation = field.getAnnotation(Must.class);
		if (annotation != null) {
			metadata.setMustInsert(annotation.insert());
			metadata.setMustInsert(annotation.update());
		}

		columnMap.put(metadata.getFieldName(), metadata);
		fieldMap.put(metadata.getFieldName(), metadata);
		logger.debug("普通字段: {} <==> {}", metadata.getFieldName(), metadata.getColumnName());
	}
}
