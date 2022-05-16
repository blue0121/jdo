package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser2.EntityMetadata;
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
	private Class<?> targetClass;
	private EntityMetadata metadata;
	private String field;
	private Map<String, ?> map;
	private List<?> args;
	private boolean dynamic;

	private SqlRequest() {
	}

	public static SqlRequest create(Map<String, ?> map, EntityMetadata metadata) {
		var request = new SqlRequest();
		request.map = map;
		request.targetClass = Map.class;
		request.metadata = metadata;
		request.dynamic = true;
		return request;
	}

	public static SqlRequest create(String field, Map<String, ?> map, EntityMetadata metadata) {
		var request = new SqlRequest();
		request.field = field;
		request.map = map;
		request.targetClass = Map.class;
		request.metadata = metadata;
		request.dynamic = true;
		return request;
	}

	public static SqlRequest create(Object target, EntityMetadata metadata, boolean dynamic) {
		var request = new SqlRequest();
		request.target = target;
		request.targetClass = target.getClass();
		request.metadata = metadata;
		request.dynamic = dynamic;
		return request;
	}

	public static SqlRequest create(Object target, List<?> args, EntityMetadata metadata) {
		var request = new SqlRequest();
		request.target = target;
		request.args = args;
		request.targetClass = target.getClass();
		request.metadata = metadata;
		request.dynamic = true;
		return request;
	}

	public static SqlRequest create(Class<?> clazz, List<?> args, EntityMetadata metadata) {
		var request = new SqlRequest();
		request.args = args;
		request.targetClass = clazz;
		request.metadata = metadata;
		request.dynamic = true;
		return request;
	}

	public static SqlRequest createForBatch(List<?> args, EntityMetadata metadata) {
		var request = new SqlRequest();
		request.args = args;
		request.targetClass = metadata.getTargetClass();
		request.metadata = metadata;
		request.dynamic = false;
		return request;
	}

}
