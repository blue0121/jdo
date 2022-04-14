package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser.EntityConfig;
import lombok.Getter;

import java.util.List;
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
	private List<?> args;
	private boolean dynamic;

	private SqlRequest() {
	}

	public static SqlRequest create(Map<String, ?> map, EntityConfig config) {
		var request = new SqlRequest();
		request.map = map;
		request.clazz = Map.class;
		request.config = config;
		request.dynamic = true;
		return request;
	}

	public static SqlRequest create(String field, Map<String, ?> map, EntityConfig config) {
		var request = new SqlRequest();
		request.field = field;
		request.map = map;
		request.clazz = Map.class;
		request.config = config;
		request.dynamic = true;
		return request;
	}

	public static SqlRequest create(Object target, EntityConfig config, boolean dynamic) {
		var request = new SqlRequest();
		request.target = target;
		request.clazz = target.getClass();
		request.config = config;
		request.dynamic = dynamic;
		return request;
	}

	public static SqlRequest create(Object target, List<?> args, EntityConfig config) {
		var request = new SqlRequest();
		request.target = target;
		request.args = args;
		request.clazz = target.getClass();
		request.config = config;
		request.dynamic = true;
		return request;
	}

	public static SqlRequest create(Class<?> clazz, List<?> args, EntityConfig config) {
		var request = new SqlRequest();
		request.args = args;
		request.clazz = clazz;
		request.config = config;
		request.dynamic = true;
		return request;
	}

	public static SqlRequest createForBatch(List<?> args, EntityConfig config) {
		var request = new SqlRequest();
		request.args = args;
		request.clazz = config.getClazz();
		request.config = config;
		request.dynamic = false;
		return request;
	}

}
