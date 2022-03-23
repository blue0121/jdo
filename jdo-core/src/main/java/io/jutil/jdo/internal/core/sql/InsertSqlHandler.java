package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlItem;
import io.jutil.jdo.internal.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class InsertSqlHandler extends AbstractSqlHandler implements SqlHandler {
	public InsertSqlHandler() {
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
			var column = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			columnList.add(column);
			fieldList.add(entry.getKey());
		}
		var sql = String.format(INSERT_TPL, config.getEscapeTableName(),
				StringUtil.join(columnList, SEPARATOR),
				StringUtil.repeat(PLACEHOLDER, columnList.size(), SEPARATOR));
		return new DefaultSqlItem(sql, fieldList);
	}

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getConfig();
		var map = response.toParamMap();

		var idMap = config.getIdMap();
		var columnMap = config.getColumnMap();
		var version = config.getVersionConfig();
		List<String> columnList = new ArrayList<>();
		for (var entry : map.entrySet()) {
			var column = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			columnList.add(column);
			response.addName(entry.getKey());
		}
		var sql = String.format(INSERT_TPL, config.getEscapeTableName(),
				StringUtil.join(columnList, SEPARATOR),
				StringUtil.repeat(PLACEHOLDER, columnList.size(), SEPARATOR));
		response.setSql(sql);
	}
}
