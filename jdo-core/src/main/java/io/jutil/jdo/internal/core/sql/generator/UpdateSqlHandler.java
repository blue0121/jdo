package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlConst;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.util.AssertUtil;
import io.jutil.jdo.internal.core.util.StringUtil;
import io.jutil.jdo.internal.core.util.VersionUtil;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@NoArgsConstructor
public class UpdateSqlHandler extends AbstractSqlHandler {


	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getMetadata();
		var map = response.toParamMap();
		AssertUtil.notEmpty(map, "参数");

		if (!request.isDynamic()) {
			var sql = config.getSqlMetadata();
			var sqlItem = VersionUtil.isForce(config) ? sql.getUpdateByIdAndVersion() : sql.getUpdateById();
			response.setSql(sqlItem.getSql());
			sqlItem.getParameterNameList().forEach(response::addName);
			return;
		}

		var idMap = config.getIdMap();
		var columnMap = config.getColumnMap();
		var version = config.getVersionMetadata();
		List<String> columnList = new ArrayList<>();
		for (var entry : map.entrySet()) {
			var column = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			if (columnMap.containsKey(entry.getKey())) {
				columnList.add(column + SqlConst.EQUAL_PLACEHOLDER);
				response.addName(entry.getKey());
			} else if (version != null && version.getFieldName().equals(entry.getKey())) {
				columnList.add(column + SqlConst.EQUAL + column + "+1");
			}
		}
		if (columnList.isEmpty()) {
			throw new JdbcException("@Column 不能为空");
		}

		List<String> idList = new ArrayList<>();
		for (var entry : map.entrySet()) {
			var id = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			if (!columnMap.containsKey(entry.getKey())) {
				idList.add(id + SqlConst.EQUAL_PLACEHOLDER);
				response.addName(entry.getKey());
			}
		}
		if (idList.isEmpty()) {
			throw new JdbcException("@Id 不能为空");
		}

		var sql = String.format(SqlConst.UPDATE_TPL, config.getEscapeTableName(),
				StringUtil.join(columnList, SqlConst.SEPARATOR),
				StringUtil.join(idList, SqlConst.AND));
		response.setSql(sql);
	}
}
