package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlItem;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlParam;
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
public class UpdateSqlHandler extends AbstractSqlHandler implements SqlHandler {


    @Override
    public SqlItem sql(SqlParam param) {
		var config = param.getEntityConfig();
		var map = param.getMap();
		this.checkMap(map);

	    var idMap = config.getIdMap();
		var columnMap = config.getColumnMap();
		var version = config.getVersionConfig();
	    List<String> columnList = new ArrayList<>();
		List<String> fieldList = new ArrayList<>();
		for (var entry : map.entrySet()) {
			var column = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			if (columnMap.containsKey(entry.getKey())) {
				columnList.add(column + EQUAL_PLACEHOLDER);
				fieldList.add(entry.getKey());
			} else if (version != null && version.getFieldName().equals(entry.getKey())) {
				columnList.add(column + EQUAL + column + "+1");
			}
		}
		if (columnList.isEmpty()) {
			throw new JdbcException("@Column 不能为空");
		}

		List<String> idList = new ArrayList<>();
		for (var entry : map.entrySet()) {
			var id = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			if (!columnMap.containsKey(entry.getKey())) {
				idList.add(id + EQUAL_PLACEHOLDER);
				fieldList.add(entry.getKey());
			}
		}
	    if (idList.isEmpty()) {
		    throw new JdbcException("@Id 不能为空");
	    }

		var sql = String.format(UPDATE_TPL, config.getEscapeTableName(),
				StringUtil.join(columnList, SEPARATOR),
				StringUtil.join(idList, AND));
        return new DefaultSqlItem(sql, fieldList);
    }

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getConfig();
		var map = response.toParamMap();

		var idMap = config.getIdMap();
		var columnMap = config.getColumnMap();
		var version = config.getVersionConfig();
		List<String> columnList = new ArrayList<>();
		for (var entry : map.entrySet()) {
			var column = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			if (columnMap.containsKey(entry.getKey())) {
				columnList.add(column + EQUAL_PLACEHOLDER);
				response.addName(entry.getKey());
			} else if (version != null && version.getFieldName().equals(entry.getKey())) {
				columnList.add(column + EQUAL + column + "+1");
			}
		}
		if (columnList.isEmpty()) {
			throw new JdbcException("@Column 不能为空");
		}

		List<String> idList = new ArrayList<>();
		for (var entry : map.entrySet()) {
			var id = this.getColumnString(entry.getKey(), idMap, columnMap, version);
			if (!columnMap.containsKey(entry.getKey())) {
				idList.add(id + EQUAL_PLACEHOLDER);
				response.addName(entry.getKey());
			}
		}
		if (idList.isEmpty()) {
			throw new JdbcException("@Id 不能为空");
		}

		var sql = String.format(UPDATE_TPL, config.getEscapeTableName(),
				StringUtil.join(columnList, SEPARATOR),
				StringUtil.join(idList, AND));
		response.setSql(sql);
	}
}
