package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.core.annotation.Must;
import io.jutil.jdo.core.annotation.Transient;
import io.jutil.jdo.core.annotation.Version;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.ColumnConfig;
import io.jutil.jdo.core.parser.IdConfig;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.core.parser.VersionConfig;
import io.jutil.jdo.core.reflect.BeanField;
import io.jutil.jdo.core.reflect.JavaBean;
import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.parser.model.DefaultColumnConfig;
import io.jutil.jdo.internal.core.parser.model.DefaultEntityConfig;
import io.jutil.jdo.internal.core.parser.model.DefaultIdConfig;
import io.jutil.jdo.internal.core.parser.model.DefaultVersionConfig;
import io.jutil.jdo.internal.core.sql.generator.DefaultSqlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class EntityParser extends AbstractParser {
	private static Logger logger = LoggerFactory.getLogger(EntityParser.class);
	private static final Set<Class<?>> INT_TYPE_SET = Set.of(int.class, long.class, Integer.class, Long.class);

	public EntityParser(Dialect dialect, boolean escape, ConfigCache cache) {
		super(dialect, escape, cache);
	}

	@Override
	protected void parseInternal(JavaBean bean) {
		Entity annotationEntity = bean.getDeclaredAnnotation(Entity.class);
		String tableName = annotationEntity.table();
		if (tableName.isEmpty()) {
			tableName = bean.getColumnName();
		}

		String escapeTableName = escape ? dialect.escape(tableName) : tableName;
		logger.info("实体类映射: {} <==> {}", bean.getTargetClass().getName(), tableName);
		DefaultEntityConfig config = new DefaultEntityConfig();
		config.setTableName(tableName);
		config.setEscapeTableName(escapeTableName);
		config.setJavaBean(bean);
		Map<String, IdConfig> idMap = new LinkedHashMap<>();
		Map<String, ColumnConfig> columnMap = new LinkedHashMap<>();
		Map<String, ColumnConfig> extraMap = new LinkedHashMap<>();

		var fieldMap = bean.getAllFields();
		for (var entry : fieldMap.entrySet()) {
			BeanField field = entry.getValue();
			Id annotationId = field.getDeclaredAnnotation(Id.class);
			if (annotationId != null) {
				IdConfig id = this.parseFieldId(field, annotationId);
				idMap.put(id.getFieldName(), id);
				logger.debug("主键字段: {} <==> {}", id.getFieldName(), id.getColumnName());
				continue;
			}
			Version annotationVersion = field.getDeclaredAnnotation(Version.class);
			if (annotationVersion != null) {
				VersionConfig version = this.parseFieldVersion(field, annotationVersion);
				config.setVersionConfig(version);
				logger.debug("版本字段: {} <==> {}", version.getFieldName(), version.getColumnName());
				continue;
			}
			Transient annotationTransient = field.getDeclaredAnnotation(Transient.class);
			ColumnConfig column = this.parseFieldColumn(entry.getValue());
			if (annotationTransient != null) {
				extraMap.put(column.getFieldName(), column);
				logger.debug("额外字段: {} <==> {}", column.getFieldName(), column.getColumnName());
			} else {
				columnMap.put(column.getFieldName(), column);
				logger.debug("普通字段: {} <==> {}", column.getFieldName(), column.getColumnName());
			}
		}

		config.setIdMap(idMap);
		config.setColumnMap(columnMap);
		config.setExtraMap(extraMap);

		var generator = new DefaultSqlGenerator(config);
		generator.generate();

		config.check();
		configCache.put(config);
	}

	private IdConfig parseFieldId(BeanField field, Id annotation) {
		DefaultIdConfig config = new DefaultIdConfig();

		Class<?> fieldType = field.getField().getType();
		IdType idType = switch (fieldType.getSimpleName()) {
			case "String" -> IdType.STRING;
			case "int", "Integer" -> IdType.INT;
			case "long", "Long" -> IdType.LONG;
			default -> throw new JdbcException("Unsupported type: " + fieldType.getName());
		};
		config.setIdType(idType);

		GeneratorType generatorType = annotation.generator();
		if (generatorType == GeneratorType.AUTO) {
			generatorType = switch (idType) {
				case STRING -> GeneratorType.UUID;
				default -> GeneratorType.INCREMENT;
			};
		}
		config.setGeneratorType(generatorType);

		this.setFieldConfig(field, config);
		return config;
	}

	private VersionConfig parseFieldVersion(BeanField field, Version annotation) {
		var type = field.getField().getType();
		if (!INT_TYPE_SET.contains(type)) {
			throw new JdbcException("@Version 类型必需是: " + INT_TYPE_SET);
		}

		DefaultVersionConfig config = new DefaultVersionConfig();
		config.setForce(annotation.force());
		config.setDefaultValue(annotation.defaultValue());
		this.setFieldConfig(field, config);
		return config;
	}

	private ColumnConfig parseFieldColumn(BeanField field) {
		DefaultColumnConfig config = new DefaultColumnConfig();
		this.setFieldConfig(field, config);

		Must annotation = field.getDeclaredAnnotation(Must.class);
		if (annotation != null) {
			config.setMustInsert(annotation.insert());
			config.setMustInsert(annotation.update());
		}

		return config;
	}

}
