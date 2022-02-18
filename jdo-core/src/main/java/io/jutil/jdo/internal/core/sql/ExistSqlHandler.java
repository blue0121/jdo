package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlItem;
import io.jutil.jdo.internal.core.util.ObjectUtil;
import io.jutil.jdo.internal.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class ExistSqlHandler implements SqlHandler {
	public ExistSqlHandler() {
	}

	@Override
	public SqlItem sql(SqlParam param) {
		List<String> args = param.getArgs() == null ? List.of() : param.getArgs();
		var map = param.getMap();
		var config = param.getEntityConfig();
		List<String> columnList = new ArrayList<>();
		List<String> fieldList = new ArrayList<>();

		if (!args.isEmpty()) {
			var columnMap = config.getColumnMap();
			for (var arg : args) {
				var whereColumn = this.getColumnString(arg, null, columnMap, null);
				columnList.add(whereColumn + EQUAL_PLACEHOLDER);
				fieldList.add(arg);
			}
		}
		String op = args.isEmpty() ? EQUAL_PLACEHOLDER : NOT_EQUAL_PLACEHOLDER;
		var idMap = config.getIdMap();
		for (var entry : idMap.entrySet()) {
			var id = entry.getValue();
			var value = map.get(entry.getKey());
			if (!ObjectUtil.isEmpty(value)) {
				columnList.add(id.getEscapeColumnName() + op);
				fieldList.add(id.getFieldName());
			}
		}
		if (columnList.isEmpty()) {
			throw new JdbcException("普通字段或主键字段不能为空");
		}
		var sql = String.format(COUNT_TPL, config.getEscapeTableName(), StringUtil.join(columnList, AND));
		return new DefaultSqlItem(sql, fieldList);
	}
}