package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.MapperConfig;
import io.jutil.jdo.internal.core.util.AssertUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class MapperConfigCache {
	private Map<Class<?>, MapperConfig> mapperCache = new HashMap<>();

	public MapperConfigCache() {
	}

	public void put(MapperConfig config) {
		AssertUtil.notNull(config, "MapperConfig");
		mapperCache.put(config.getClazz(), config);
	}

	public boolean exist(Class<?> clazz) {
		AssertUtil.notNull(clazz, "Class");
		return mapperCache.containsKey(clazz);
	}

	public MapperConfig get(Class<?> clazz) {
		if (!this.exist(clazz)) {
			throw new JdbcException("Not found @Mapper at " + clazz.getName());
		}
		return mapperCache.get(clazz);
	}

	public Map<Class<?>, MapperConfig> all() {
		return Map.copyOf(mapperCache);
	}

}
