package io.jutil.jdo.internal.core.executor.parameter;

import io.jutil.jdo.core.parser.FieldMetadata;
import io.jutil.jdo.internal.core.sql.SqlParameter;
import lombok.Getter;

import java.sql.PreparedStatement;

/**
 * @author Jin Zheng
 * @since 2022-05-30
 */
@Getter
public class BindContext<T> {
	private PreparedStatement preparedStatement;
	private int index;
	private FieldMetadata metadata;
	private T value;

	private BindContext() {
	}

	@SuppressWarnings("unchecked")
	public static <T> BindContext<T> create(PreparedStatement preparedStatement, int index, SqlParameter parameter) {
		var context = new BindContext<T>();
		context.preparedStatement = preparedStatement;
		context.index = index;
		context.metadata = parameter.getMetadata();
		context.value = (T) parameter.getValue();
		return context;
	}

}
