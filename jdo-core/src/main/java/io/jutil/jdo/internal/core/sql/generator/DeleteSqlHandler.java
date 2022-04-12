package io.jutil.jdo.internal.core.sql.generator;


import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlItem;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlConst;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlParam;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.util.AssertUtil;
import io.jutil.jdo.internal.core.util.IdUtil;
import io.jutil.jdo.internal.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class DeleteSqlHandler extends AbstractSqlHandler implements SqlHandler {
	public DeleteSqlHandler() {
	}

	@Override
	public SqlItem sql(SqlParam param) {
		var config = param.getEntityConfig();
		var map = param.getMap();
		this.checkMap(map);

		var idMap = config.getIdMap();
		var columnMap = config.getColumnMap();
		var version = config.getVersionConfig();
		List<String> columnList = new ArrayList<>();
		List<String> fieldList = new ArrayList<>();
		for (var entry : map.entrySet()) {
			var whereColumn = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			columnList.add(whereColumn + EQUAL_PLACEHOLDER);
			fieldList.add(entry.getKey());
		}
		var sql = String.format(DELETE_TPL, config.getEscapeTableName(), StringUtil.join(columnList, AND));
		return new DefaultSqlItem(sql, fieldList);
	}

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getConfig();
		var args = request.getArgs();
		AssertUtil.notEmpty(args, "Args");

		var id = IdUtil.checkSingleId(config);
		if (args.size() == 1) {
			var where = id.getEscapeColumnName() + SqlConst.EQUAL_PLACEHOLDER;
			var sql = String.format(SqlConst.DELETE_TPL, config.getEscapeTableName(), where);
			response.setSql(sql);
			response.addParam(args.get(0));
			return;
		}

		var placeholder = StringUtil.repeat(SqlConst.PLACEHOLDER, args.size(), SqlConst.SEPARATOR);
		var where = id.getEscapeColumnName() + String.format(SqlConst.IN_PLACEHOLDER, placeholder);
		var sql = String.format(SqlConst.DELETE_TPL, config.getEscapeTableName(), where);
		response.setSql(sql);
		for (var arg : args) {
			response.addParam(arg);
		}
	}
}
