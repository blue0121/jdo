package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlItem;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlParam;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.util.ObjectUtil;
import io.jutil.jdo.internal.core.util.StringUtil;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@NoArgsConstructor
public class ExistSqlHandler extends AbstractSqlHandler implements SqlHandler {


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
			throw new JdbcException("@Column 或 @Id 不能为空");
		}
		var sql = String.format(COUNT_TPL, config.getEscapeTableName(), StringUtil.join(columnList, AND));
		return new DefaultSqlItem(sql, fieldList);
	}

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		List<?> args = request.getArgs() == null ? List.of() : request.getArgs();
		var map = response.toParamMap();
		var config = request.getConfig();

		List<String> columnList = new ArrayList<>();
		if (!args.isEmpty()) {
			var columnMap = config.getColumnMap();
			for (var arg : args) {
				var strArg = arg.toString();
				var whereColumn = this.getColumnString(strArg, null, columnMap, null);
				columnList.add(whereColumn + EQUAL_PLACEHOLDER);
				response.addName(strArg);
			}
		}

		String op = args.isEmpty() ? EQUAL_PLACEHOLDER : NOT_EQUAL_PLACEHOLDER;
		var idMap = config.getIdMap();
		for (var entry : idMap.entrySet()) {
			var id = entry.getValue();
			var value = map.get(entry.getKey());
			if (!ObjectUtil.isEmpty(value)) {
				columnList.add(id.getEscapeColumnName() + op);
				response.addName(id.getFieldName());
			}
		}
		if (columnList.isEmpty()) {
			throw new JdbcException("@Column 或 @Id 不能为空");
		}
		var sql = String.format(COUNT_TPL, config.getEscapeTableName(), StringUtil.join(columnList, AND));
		response.setSql(sql);
	}
}
