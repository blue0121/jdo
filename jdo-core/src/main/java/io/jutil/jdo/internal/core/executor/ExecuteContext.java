package io.jutil.jdo.internal.core.executor;

import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.internal.core.sql.SqlParameter;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import lombok.Getter;

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
		var context = new ExecuteContext();
		context.metadata = response.getMetadata();
		context.sql = response.getSql();
		context.parameterList = response.toParameterList();
		context.batchParameterList = response.toBatchParameterList();
		return context;
	}

}
