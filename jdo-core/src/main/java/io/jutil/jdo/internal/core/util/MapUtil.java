package io.jutil.jdo.internal.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-20
 */
public class MapUtil {
	private MapUtil() {
	}

    public static Map<String, Object> merge(Map<String, ?> origin, String key, Object value) {
		Map<String, Object> map = new HashMap<>();
		for (var entry : origin.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		map.put(key, value);
		return map;
    }
}
