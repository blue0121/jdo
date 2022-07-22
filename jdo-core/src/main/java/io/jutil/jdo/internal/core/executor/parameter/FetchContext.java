package io.jutil.jdo.internal.core.executor.parameter;

import io.jutil.jdo.core.parser.FieldMetadata;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @author Jin Zheng
 * @since 2022-05-30
 */
@Getter
public class FetchContext {
	private ResultSetMetaData resultSetMetaData;
	private ResultSet resultSet;
	private int index;
	private FieldMetadata fieldMetadata;

	private FetchContext() {
	}

	public static FetchContext create(ResultSetMetaData resultSetMetaData, ResultSet resultSet, int index,
	                                  FieldMetadata metadata) {
		var context = new FetchContext();
		context.resultSetMetaData = resultSetMetaData;
		context.resultSet = resultSet;
		context.index = index;
		context.fieldMetadata = metadata;
		return context;
	}

}
