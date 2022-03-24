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
public class CountSqlHandler extends AbstractSqlHandler implements SqlHandler {
	public CountSqlHandler() {
	}

	@Override
	public SqlItem sql(SqlParam param) {
		var config = param.getEntityConfig();
		var map = param.getMap();
		this.checkMap(map);

		List<String> columnList = new ArrayList<>();
		List<String> fieldList = new ArrayList<>();
		var columnMap = config.getColumnMap();
		var idMap = config.getIdMap();
		for (var entry : map.entrySet()) {
			String whereColumn = this.getColumnString(entry.getKey(), idMap, columnMap, null);
			columnList.add(whereColumn + EQUAL_PLACEHOLDER);
			fieldList.add(entry.getKey());
		}
		var sql = String.format(COUNT_TPL, config.getEscapeTableName(), StringUtil.join(columnList, AND));
		return new DefaultSqlItem(sql, fieldList);
	}

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getConfig();
		var map = response.toParamMap();

		List<String> columnList = new ArrayList<>();
		var columnMap = config.getColumnMap();
		var idMap = config.getIdMap();
		for (var entry : map.entrySet()) {
			String whereColumn = this.getColumnString(entry.getKey(), idMap, columnMap, null);
			columnList.add(whereColumn + EQUAL_PLACEHOLDER);
			response.addName(entry.getKey());
		}
		var sql = String.format(COUNT_TPL, config.getEscapeTableName(), StringUtil.join(columnList, AND));
		response.setSql(sql);
	}
}
