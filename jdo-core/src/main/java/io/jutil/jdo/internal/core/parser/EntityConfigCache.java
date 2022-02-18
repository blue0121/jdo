package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.internal.core.util.AssertUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class EntityConfigCache {
	private Map<Class<?>, EntityConfig> entityCache = new HashMap<>();

	public EntityConfigCache() {
	}

	public void put(EntityConfig config) {
		AssertUtil.notNull(config, "EntityConfig");
		entityCache.put(config.getClazz(), config);
	}

	public boolean exist(Class<?> clazz) {
		AssertUtil.notNull(clazz, "Class");
		return entityCache.containsKey(clazz);
	}

	public EntityConfig get(Class<?> clazz) {
		if (!this.exist(clazz)) {
			throw new JdbcException("Not found @Entity at " + clazz.getName());
		}
		return entityCache.get(clazz);
	}

	public Map<Class<?>, EntityConfig> all() {
		return Map.copyOf(entityCache);
	}

}
