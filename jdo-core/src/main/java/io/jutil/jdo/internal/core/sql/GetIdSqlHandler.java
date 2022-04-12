package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.internal.core.util.AssertUtil;
import io.jutil.jdo.internal.core.util.IdUtil;
import io.jutil.jdo.internal.core.util.StringUtil;

/**
 * @author Jin Zheng
 * @since 2022-04-12
 */
public class GetIdSqlHandler extends AbstractSqlHandler {
	public GetIdSqlHandler() {
	}

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getConfig();
		var args = request.getArgs();
		AssertUtil.notEmpty(args, "Args");

		var id = IdUtil.checkSingleId(config);
		if (args.size() == 1) {
			var where = id.getEscapeColumnName() + SqlConst.EQUAL_PLACEHOLDER;
			var sql = String.format(SqlConst.SELECT_TPL, config.getEscapeTableName(), where);
			response.setSql(sql);
			response.addParam(args.get(0));
			return;
		}

		var placeholder = StringUtil.repeat(SqlConst.PLACEHOLDER, args.size(), SqlConst.SEPARATOR);
		var where = id.getEscapeColumnName() + String.format(SqlConst.IN_PLACEHOLDER, placeholder);
		var sql = String.format(SqlConst.SELECT_TPL, config.getEscapeTableName(), where);
		response.setSql(sql);
		for (var arg : args) {
			response.addParam(arg);
		}
	}
}
