package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.ConfigType;
import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.MapperConfig;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-11
 */
@NoArgsConstructor
public class ConfigCache {
	private final Map<Class<?>, MapperConfig> configMap = new HashMap<>();


	public void put(MapperConfig config) {
		configMap.put(config.getClazz(), config);
	}

	public boolean exist(Class<?> clazz) {
		return configMap.containsKey(clazz);
	}

	public boolean exist(Class<?> clazz, ConfigType type) {
		var config = configMap.get(clazz);
		if (config != null) {
			return config.getConfigType() == type;
		}
		return false;
	}

	public MapperConfig get(Class<?> clazz) {
		return configMap.get(clazz);
	}

	public EntityConfig getEntityConfig(Class<?> clazz) {
		var config = configMap.get(clazz);
		if (config instanceof EntityConfig c) {
			return c;
		}
		return null;
	}

	public MapperConfig load(Class<?> clazz) {
		var config = configMap.get(clazz);
		if (config == null) {
			throw new JdbcException("找不到配置: " + clazz.getName());
		}
		return config;
	}

	public EntityConfig loadEntityConfig(Class<?> clazz) {
		var config = this.getEntityConfig(clazz);
		if (config == null) {
			throw new JdbcException("找不到实体配置: " + clazz.getName());
		}
		return config;
	}

	public Map<Class<?>, MapperConfig> all() {
		return Map.copyOf(configMap);
	}

	public Map<Class<?>, EntityConfig> allEntityConfig() {
		Map<Class<?>, EntityConfig> map = new HashMap<>();
		for (var entry : configMap.entrySet()) {
			if (entry.getValue() instanceof EntityConfig c) {
				map.put(entry.getKey(), c);
			}
		}
		return map;
	}

}
