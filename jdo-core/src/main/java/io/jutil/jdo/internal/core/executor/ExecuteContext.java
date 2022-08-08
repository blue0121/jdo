package io.jutil.jdo.internal.core.executor;

import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.internal.core.sql.SqlParameter;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-30
 */
@Getter
public class ExecuteContext {
	private EntityMetadata metadata;
    private String sql;
	private List<SqlParameter> parameterList;
	private List<List<SqlParameter>> batchParameterList;

	private ExecuteContext() {
	}

	public static ExecuteContext create(SqlResponse response) {
		return create(response.getSql(), response);
	}

	public static ExecuteContext create(String sql, SqlResponse response) {
		var context = new ExecuteContext();
		context.metadata = response.getMetadata();
		context.sql = sql;
		context.parameterList = response.toParameterList();


		context.batchParameterList = response.toBatchParameterList();
		return context;
	}

	public static ExecuteContext create(String sql, List<?> parameterList) {
		var context = new ExecuteContext();
		context.sql = sql;
		context.parameterList = new ArrayList<>();
		if (parameterList != null && !parameterList.isEmpty()) {
			for (var parameter : parameterList) {
				context.parameterList.add(SqlParameter.create(parameter));
			}
		}
		return context;
	}

}
