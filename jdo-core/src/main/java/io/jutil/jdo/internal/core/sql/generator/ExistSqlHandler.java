package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlConst;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.util.StringUtil;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@NoArgsConstructor
public class ExistSqlHandler extends AbstractSqlHandler {


	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		List<?> args = request.getArgs() == null ? List.of() : request.getArgs();
		var map = response.toParamMap();
		var config = request.getMetadata();

		List<String> columnList = new ArrayList<>();
		if (!args.isEmpty()) {
			var columnMap = config.getColumnMap();
			for (var arg : args) {
				var strArg = arg.toString();
				var whereColumn = this.getColumnString(strArg, null, columnMap, null);
				columnList.add(whereColumn + SqlConst.EQUAL_PLACEHOLDER);
				response.addName(strArg);
			}
		}

		String op = args.isEmpty() ? SqlConst.EQUAL_PLACEHOLDER : SqlConst.NOT_EQUAL_PLACEHOLDER;
		var idMap = config.getIdMap();
		for (var entry : idMap.entrySet()) {
			var id = entry.getValue();
			var value = map.get(entry.getKey());
			if (!this.isEmpty(value)) {
				columnList.add(id.getEscapeColumnName() + op);
				response.addName(id.getFieldName());
			}
		}
		if (columnList.isEmpty()) {
			throw new JdbcException("@Column 或 @Id 不能为空");
		}
		var sql = String.format(SqlConst.COUNT_TPL, config.getEscapeTableName(), StringUtil.join(columnList, SqlConst.AND));
		response.setSql(sql);
	}
}
