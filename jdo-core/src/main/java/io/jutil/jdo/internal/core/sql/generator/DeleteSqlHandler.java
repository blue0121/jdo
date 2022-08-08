package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlConst;
import io.jutil.jdo.internal.core.sql.SqlParameter;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.util.AssertUtil;
import io.jutil.jdo.internal.core.util.IdUtil;
import io.jutil.jdo.internal.core.util.StringUtil;
import lombok.NoArgsConstructor;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@NoArgsConstructor
public class DeleteSqlHandler extends AbstractSqlHandler {


	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getMetadata();
		var args = request.getArgs();
		AssertUtil.notEmpty(args, "参数");

		var id = IdUtil.checkSingleId(config);
		if (args.size() == 1) {
			var sqlItem = config.getSqlMetadata().getDeleteById();
			response.setSql(sqlItem.getSql());
			response.addParam(args.get(0));
			response.addParameter(SqlParameter.create(args.get(0)));
			return;
		}

		var sqlItem = config.getSqlMetadata().getDeleteByIdList();
		var sql = String.format(sqlItem.getSql(), StringUtil.repeat(SqlConst.PLACEHOLDER, args.size(), SqlConst.SEPARATOR));
		response.setSql(sql);
		for (var arg : args) {
			response.addParam(arg);
			response.addParameter(SqlParameter.create(arg));
		}
	}
}
