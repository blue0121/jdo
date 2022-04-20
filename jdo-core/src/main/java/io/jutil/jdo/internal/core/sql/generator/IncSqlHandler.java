package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.core.exception.EntityFieldException;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlItem;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlParam;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.util.NumberUtil;
import io.jutil.jdo.internal.core.util.StringUtil;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@NoArgsConstructor
public class IncSqlHandler extends AbstractSqlHandler implements SqlHandler {


	@Override
	public SqlItem sql(SqlParam param) {
		var config = param.getEntityConfig();
		var map = param.getMap();
		this.checkMap(map);

		var columnMap = config.getColumnMap();
		List<String> columnList = new ArrayList<>();
		List<String> fieldList = new ArrayList<>();

		for (var entry : map.entrySet()) {
			var column = this.getColumn(entry.getKey(), columnMap);
			if (!NumberUtil.isNumber(column.getBeanField().getField().getType())) {
				throw new JdbcException("字段 [" + entry.getKey() + "] 不是数字");
			}
			if (!NumberUtil.isNumber(entry.getValue().getClass())) {
				throw new JdbcException("参数 [" + entry.getKey() + "] 不是数字");
			}
			columnList.add(column.getEscapeColumnName() + "=" + column.getEscapeColumnName() + "+?");
			fieldList.add(entry.getKey());
		}

		var id = config.getIdConfig();
		var whereId = id.getEscapeColumnName() + EQUAL_PLACEHOLDER;
		fieldList.add(id.getFieldName());

		var sql = String.format(UPDATE_TPL, config.getEscapeTableName(),
				StringUtil.join(columnList, SEPARATOR), whereId);
		return new DefaultSqlItem(sql, fieldList);
	}

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getConfig();
		var map = response.toParamMap();

		var columnMap = config.getColumnMap();
		List<String> columnList = new ArrayList<>();

		for (var entry : map.entrySet()) {
			var column = columnMap.get(entry.getKey());
			if (column == null) {
				continue;
			}
			if (!NumberUtil.isNumber(column.getBeanField().getField().getType())) {
				throw new EntityFieldException(entry.getKey(), "不是数字");
			}
			if (!NumberUtil.isNumber(entry.getValue().getClass())) {
				throw new EntityFieldException(entry.getKey(), "不是数字");
			}
			columnList.add(column.getEscapeColumnName() + "=" + column.getEscapeColumnName() + "+?");
			response.addName(entry.getKey());
		}

		var id = config.getIdConfig();
		var whereId = id.getEscapeColumnName() + EQUAL_PLACEHOLDER;
		response.addName(id.getFieldName());

		var sql = String.format(UPDATE_TPL, config.getEscapeTableName(),
				StringUtil.join(columnList, SEPARATOR), whereId);
		response.setSql(sql);
	}
}
