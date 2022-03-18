package io.jutil.jdo.internal.core.executor.mapper;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.internal.core.util.AssertUtil;
import lombok.Getter;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
@Getter
public class MapRequest {
	private Object target;
	private Class<?> clazz;
	private EntityConfig config;
	private Map<String, Object> map;
	private boolean dynamic;

	private MapRequest() {
	}

	public static MapRequest create(Object target, EntityConfig config, boolean dynamic) {
		AssertUtil.notNull(target, "对象");
		AssertUtil.notNull(config, "EntityConfig");
		var request = new MapRequest();
		if (target instanceof Class c) {
			request.clazz = c;
		} else if (target instanceof Map m) {
			request.map = m;
			request.clazz = Map.class;
		}else {
			request.target = target;
			request.clazz = target.getClass();
		}
		request.config = config;
		request.dynamic = dynamic;
		return request;
	}

}
