package io.jutil.jdo.internal.core.sql;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class ParamSqlHandler extends AbstractSqlHandler {
	public ParamSqlHandler() {
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
