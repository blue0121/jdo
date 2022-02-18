package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.MapperConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@Getter
@Setter
@NoArgsConstructor
public class SqlParam {
	private EntityConfig entityConfig;
	private MapperConfig mapperConfig;
	private Class<?> clazz;
	private Object target;
	private String field;
	private Map<String, ?> map;
	private List<String> args;

}
