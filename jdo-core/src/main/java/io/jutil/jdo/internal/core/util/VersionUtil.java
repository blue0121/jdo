package io.jutil.jdo.internal.core.util;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.VersionConfig;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class VersionUtil {
	private VersionUtil() {
	}

	public static void generate(Map<String, Object> map, VersionConfig config) {
		if (config == null) {
			return;
		}

		Object value = map.get(config.getFieldName());
		if (value == null) {
			map.put(config.getFieldName(), config.getDefaultValue());
		}
	}


	public static boolean isForce(EntityConfig config) {
		var verConfig = config.getVersionConfig();
		if (verConfig == null) {
			return false;
		}
		return verConfig.isForce();
	}

}
