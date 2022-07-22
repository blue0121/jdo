package io.jutil.jdo.internal.core.executor.parameter;

import io.jutil.jdo.core.parser.FieldMetadata;
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
	private T value;
	private FieldMetadata fieldMetadata;

	private BindContext() {
	}

	public static <T> BindContext<T> create(PreparedStatement preparedStatement, int index, T value,
	                                        FieldMetadata metadata) {
		var context = new BindContext<T>();
		context.preparedStatement = preparedStatement;
		context.index = index;
		context.value = value;
		context.fieldMetadata = metadata;
		return context;
	}

}
