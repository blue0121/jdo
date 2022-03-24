package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.internal.core.util.AssertUtil;
import lombok.Getter;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-21
 */
@Getter
public class SqlRequest {
	private Object target;
	private Class<?> clazz;
	private EntityConfig config;
	private String field;
	private Map<String, ?> map;
	private boolean dynamic;

	private SqlRequest() {
	}

	public static SqlRequest create(Map<String, ?> map, EntityConfig config) {
		AssertUtil.notNull(map, "对象");
		AssertUtil.notNull(config, "EntityConfig");
		var request = new SqlRequest();
		request.map = map;
		request.clazz = Map.class;
		request.config = config;
		request.dynamic = true;
		return request;
	}

	public static SqlRequest create(String field, Map<String, ?> map, EntityConfig config) {
		AssertUtil.notNull(map, "对象");
		AssertUtil.notNull(config, "EntityConfig");
		var request = new SqlRequest();
		request.field = field;
		request.map = map;
		request.clazz = Map.class;
		request.config = config;
		request.dynamic = true;
		return request;
	}

	public static SqlRequest create(Object target, EntityConfig config, boolean dynamic) {
		AssertUtil.notNull(target, "对象");
		AssertUtil.notNull(config, "EntityConfig");
		var request = new SqlRequest();
		request.target = target;
		request.clazz = target.getClass();
		request.config = config;
		request.dynamic = dynamic;
		return request;
	}

}
