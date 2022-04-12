package io.jutil.jdo.internal.core.sql.parameter;

import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class ParameterSqlHandler extends AbstractSqlHandler {
	public ParameterSqlHandler() {
	}

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var list = response.toNameList();
		var map = response.toParamMap();
		for (var name : list) {
			var value = map.get(name);
			response.addParam(value);
		}
	}
}
