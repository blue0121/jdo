package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.core.exception.EntityFieldException;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlConst;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.util.AssertUtil;
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
public class IncSqlHandler extends AbstractSqlHandler {


	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getConfig();
		var map = response.toParamMap();
		AssertUtil.notEmpty(map, "参数");

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
		var whereId = id.getEscapeColumnName() + SqlConst.EQUAL_PLACEHOLDER;
		response.addName(id.getFieldName());

		var sql = String.format(SqlConst.UPDATE_TPL, config.getEscapeTableName(),
				StringUtil.join(columnList, SqlConst.SEPARATOR), whereId);
		response.setSql(sql);
	}
}
