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
public class GetSqlHandler extends AbstractSqlHandler {


	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getMetadata();
		var map = response.toParamMap();
		AssertUtil.notEmpty(map, "参数");

		var columnMap = config.getColumnMap();
		var idMap = config.getIdMap();
		var version = config.getVersionMetadata();
		List<String> columnList = new ArrayList<>();
		for (var entry : map.entrySet()) {
			var whereColumn = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			columnList.add(whereColumn + SqlConst.EQUAL_PLACEHOLDER);
			response.addName(entry.getKey());
		}
		var sql = String.format(SqlConst.SELECT_TPL, config.getEscapeTableName(), StringUtil.join(columnList, SqlConst.AND));
		response.setSql(sql);
	}
}
