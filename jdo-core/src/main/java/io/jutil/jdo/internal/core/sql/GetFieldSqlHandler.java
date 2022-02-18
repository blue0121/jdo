package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlItem;
import io.jutil.jdo.internal.core.util.AssertUtil;
import io.jutil.jdo.internal.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class GetFieldSqlHandler implements SqlHandler {
	public GetFieldSqlHandler() {
	}

	@Override
	public SqlItem sql(SqlParam param) {
		var field = param.getField();
		AssertUtil.notEmpty(field, "Field");
		var map = param.getMap();
		var config = param.getEntityConfig();
		this.checkMap(map);

		var columnMap = config.getColumnMap();
		var idMap = config.getIdMap();
		var version = config.getVersionConfig();
		List<String> columnList = new ArrayList<>();
		List<String> fieldList = new ArrayList<>();

		String fieldColumn = this.getColumnString(field, idMap, columnMap, version);
		for (var entry : map.entrySet()) {
			String whereColumn = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			columnList.add(whereColumn + EQUAL_PLACEHOLDER);
			fieldList.add(entry.getKey());
		}
		var sql = String.format(GET_TPL, fieldColumn, config.getEscapeTableName(), StringUtil.join(columnList, AND));
		return new DefaultSqlItem(sql, fieldList);
	}
}
