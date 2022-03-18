package io.jutil.jdo.internal.core.executor.mapper;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.internal.core.util.AssertUtil;
import lombok.Getter;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
@Getter
public class MapRequest {
	private Object target;
	private Class<?> clazz;
	private EntityConfig config;
	private boolean dynamic;

	private MapRequest() {
	}

	public static MapRequest create(Object target, EntityConfig config, boolean dynamic) {
		AssertUtil.notNull(target, "对象");
		AssertUtil.notNull(config, "EntityConfig");
		var request = new MapRequest();
		if (target instanceof Class c) {
			request.clazz = c;
		} else {
			request.target = target;
			request.clazz = target.getClass();
		}
		request.config = config;
		request.dynamic = dynamic;
		return request;
	}

}
