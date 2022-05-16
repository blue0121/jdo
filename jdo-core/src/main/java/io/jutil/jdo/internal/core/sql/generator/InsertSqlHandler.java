package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlConst;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.util.AssertUtil;
import io.jutil.jdo.internal.core.util.StringUtil;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@NoArgsConstructor
public class InsertSqlHandler extends AbstractSqlHandler {


	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getMetadata();
		var map = response.toParamMap();
		AssertUtil.notEmpty(map, "参数");

		var idMap = config.getIdMap();
		var columnMap = config.getColumnMap();
		var version = config.getVersionMetadata();
		List<String> columnList = new ArrayList<>();
		for (var entry : map.entrySet()) {
			var column = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			columnList.add(column);
			response.addName(entry.getKey());
		}
		var sql = String.format(SqlConst.INSERT_TPL, config.getEscapeTableName(),
				StringUtil.join(columnList, SqlConst.SEPARATOR),
				StringUtil.repeat(SqlConst.PLACEHOLDER, columnList.size(), SqlConst.SEPARATOR));
		response.setSql(sql);
	}
}
