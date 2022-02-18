package io.jutil.jdo.internal.core.util;

import io.jutil.jdo.core.parser.ColumnConfig;
import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.IdConfig;
import io.jutil.jdo.core.parser.VersionConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class ObjectUtil {
	private static final String EMPTY_STR = "";

	private ObjectUtil() {
	}

	public static Map<String, Object> generateMap(Map<String, ?> param, EntityConfig config) {
		Map<String, Object> map = new HashMap<>();
		if (param != null && !param.isEmpty()) {
			for (var entry : param.entrySet()) {
				map.put(entry.getKey(), convert(entry.getValue()));
			}
		}
		IdUtil.generateId(map, config.getIdMap());
		VersionUtil.generate(map, config.getVersionConfig());
		return map;
	}

	public static Map<String, Object> toMap(Object target, EntityConfig config, boolean dynamic) {
		Map<String, Object> map = new HashMap<>();
		toIdMap(target, map, config.getIdMap(), dynamic);
		toVersionMap(target, map, config.getVersionConfig());
		toColumnMap(target, map, config.getColumnMap(), dynamic);
		return map;
	}

	private static void toIdMap(Object target, Map<String, Object> map, Map<String, IdConfig> idMap, boolean dynamic) {
		for (var entry : idMap.entrySet()) {
			var id = entry.getValue();
			Object value = id.getBeanField().getFieldValue(target);
			value = convert(value);
			if (!dynamic || value != null) {
				map.put(id.getFieldName(), value);
			}
		}
	}

	private static void toVersionMap(Object target, Map<String, Object> map, VersionConfig config) {
		if (config == null) {
			return;
		}

		Object value = config.getBeanField().getFieldValue(target);
		if (value == null) {
			value = config.getDefaultValue();
		}
		map.put(config.getFieldName(), value);
	}

	private static void toColumnMap(Object target, Map<String, Object> map, Map<String, ColumnConfig> columnMap, boolean dynamic) {
		for (var entry : columnMap.entrySet()) {
			var column = entry.getValue();
			Object value = column.getBeanField().getFieldValue(target);
			value = convert(value);
			if (!dynamic || value != null) {
				map.put(column.getFieldName(), value);
			}
		}
	}

	public static Object convert(Object val) {
		if (isEmpty(val)) {
			return null;
		}
		if (val instanceof Enum e) {
			return e.name();
		}

		return val;
	}

	public static boolean isEmpty(Object object) {
		return object == null || EMPTY_STR.equals(object);
	}

}
