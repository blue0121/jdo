package io.jutil.jdo.internal.core.util;

import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.IdConfig;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.internal.core.executor.KeyHolder;
import io.jutil.jdo.internal.core.id.IdGenerator;
import io.jutil.jdo.internal.core.id.SnowflakeId;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class IdUtil {
	private IdUtil() {
	}

	/**
	 * 产生主键
	 *
	 * @return
	 */
	public static void generateId(Map<String, Object> map, Map<String, IdConfig> idMap) {
		generateId(map, idMap, null);
	}

	public static void generateId(Map<String, Object> map, Map<String, IdConfig> idMap, SnowflakeId snowflakeId) {
		for (var entry : idMap.entrySet()) {
			var id = entry.getValue();
			if (id.getGeneratorType() == GeneratorType.AUTO) {
				if (id.getIdType() == IdType.STRING) {
					map.put(id.getFieldName(), IdGenerator.uuid32bit());
				}
			} else if (id.getGeneratorType() == GeneratorType.UUID) {
				if (id.getIdType() == IdType.STRING) {
					map.put(id.getFieldName(), IdGenerator.uuid32bit());
				} else if (id.getIdType() == IdType.LONG) {
					if (snowflakeId == null) {
						map.put(id.getFieldName(), IdGenerator.id());
					} else {
						map.put(id.getFieldName(), snowflakeId.nextId());
					}
				}
			}
		}
	}

	public static void filterId(Map<String, ?> map, Map<String, IdConfig> idMap) {
		for (var entry : idMap.entrySet()) {
			var id = entry.getValue();
			if (id.isDbGenerated()) {
				map.remove(id.getFieldName());
			}
		}
	}

	public static void setId(KeyHolder holder, Object target, EntityConfig config) {
		if (holder == null || target == null || config == null) {
			return;
		}
		var id = config.getIdConfig();
		if (id == null) {
			return;
		}
		if (target instanceof Collection<?> list) {
			int i = 0;
			var numList = holder.getKeyList();
			if (numList.isEmpty()) {
				return;
			}
			for (var t : list) {
				setId(numList.get(i++), t, id);
			}
		} else {
			setId(holder.getKey(), target, id);
		}
	}

	private static void setId(Number num, Object target, IdConfig id) {
		if (num == null) {
			return;
		}
		var field = id.getBeanField();
		if (id.getIdType() == IdType.INT) {
			field.setFieldValue(target, num.intValue());
		} else if (id.getIdType() == IdType.LONG) {
			field.setFieldValue(target, num.longValue());
		}
	}

	public static IdConfig checkSingleId(EntityConfig entityConfig) {
		var id = entityConfig.getIdConfig();
		if (id == null) {
			throw new JdbcException(entityConfig.getClazz().getName() + " @Id主键个数大于1，不支持该操作");
		}

		return id;
	}

}
