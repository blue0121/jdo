package io.jutil.jdo.internal.core.parser2;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.core.annotation.Must;
import io.jutil.jdo.core.annotation.Transient;
import io.jutil.jdo.core.annotation.Version;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser2.ColumnMetadata;
import io.jutil.jdo.core.parser2.IdMetadata;
import io.jutil.jdo.core.parser2.IdType;
import io.jutil.jdo.core.parser2.MapperMetadata;
import io.jutil.jdo.core.reflect2.ClassFieldOperation;
import io.jutil.jdo.core.reflect2.ReflectFactory;
import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.parser2.model.DefaultColumnMetadata;
import io.jutil.jdo.internal.core.parser2.model.DefaultEntityMetadata;
import io.jutil.jdo.internal.core.parser2.model.DefaultIdMetadata;
import io.jutil.jdo.internal.core.parser2.model.DefaultVersionMetadata;
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

	public EntityParser(Dialect dialect, boolean escape) {
		super(dialect, escape);
	}

    @Override
    public MapperMetadata parse(Class<?> clazz) {
		var classOperation = ReflectFactory.getClassOperation(clazz);
	    Entity annotationEntity = classOperation.getAnnotation(Entity.class);
	    String tableName = annotationEntity.table();
	    if (tableName.isEmpty()) {
		    tableName = this.toColumnName(classOperation.getName());
	    }

	    String escapeTableName = escape ? dialect.escape(tableName) : tableName;
	    logger.info("实体类映射: {} <==> {}", classOperation.getTargetClass().getName(), tableName);
	    var entityMetadata = new DefaultEntityMetadata();
	    entityMetadata.setTableName(tableName);
	    entityMetadata.setEscapeTableName(escapeTableName);
	    entityMetadata.setClassOperation(classOperation);
	    Map<String, IdMetadata> idMap = new LinkedHashMap<>();
	    Map<String, ColumnMetadata> columnMap = new LinkedHashMap<>();
	    Map<String, ColumnMetadata> extraMap = new LinkedHashMap<>();

	    var fieldMap = classOperation.getAllFields();
	    for (var entry : fieldMap.entrySet()) {
		    var field = entry.getValue();
		    if (this.parseFieldId(field, idMap)) {
			    continue;
		    }
			if (this.parseFieldVersion(field, entityMetadata)) {
				continue;
			}

			this.parseFieldColumn(field, columnMap, extraMap);
	    }

	    entityMetadata.setIdMap(idMap);
	    entityMetadata.setColumnMap(columnMap);
	    entityMetadata.setExtraMap(extraMap);

	    var generator = new DefaultSqlGenerator(entityMetadata);
	    generator.generate();

	    entityMetadata.check();
        return entityMetadata;
    }

	private boolean parseFieldId(ClassFieldOperation field, Map<String, IdMetadata> idMap) {
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
		var generatorType = annotationId.generator();
		if (generatorType == GeneratorType.AUTO) {
			generatorType = switch (idType) {
				case STRING -> GeneratorType.UUID;
				default -> GeneratorType.INCREMENT;
			};
		}

		var metadata = new DefaultIdMetadata();
		this.setFieldMetadata(field, metadata);
		metadata.setIdType(idType);
		metadata.setGeneratorType(generatorType);

		idMap.put(metadata.getFieldName(), metadata);
		logger.debug("主键字段: {} <==> {}", metadata.getFieldName(), metadata.getColumnName());
		return true;
	}

	private boolean parseFieldVersion(ClassFieldOperation field, DefaultEntityMetadata entityMetadata) {
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
		logger.debug("版本字段: {} <==> {}", metadata.getFieldName(), metadata.getColumnName());
		return true;
	}

	private void parseFieldColumn(ClassFieldOperation field, Map<String, ColumnMetadata> columnMap,
	                              Map<String, ColumnMetadata> extraMap) {
		var metadata = new DefaultColumnMetadata();
		this.setFieldMetadata(field, metadata);

		Must annotation = field.getAnnotation(Must.class);
		if (annotation != null) {
			metadata.setMustInsert(annotation.insert());
			metadata.setMustInsert(annotation.update());
		}

		Transient annotationTransient = field.getAnnotation(Transient.class);
		if (annotationTransient != null) {
			extraMap.put(metadata.getFieldName(), metadata);
			logger.debug("额外字段: {} <==> {}", metadata.getFieldName(), metadata.getColumnName());
		} else {
			columnMap.put(metadata.getFieldName(), metadata);
			logger.debug("普通字段: {} <==> {}", metadata.getFieldName(), metadata.getColumnName());
		}
	}
}
