package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.ColumnConfig;
import io.jutil.jdo.core.parser.IdConfig;
import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.core.parser.VersionConfig;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public interface SqlHandler {
	String SUCCESS = "success";
	String PLACEHOLDER = "?";
	String SEPARATOR = ",";
	String EQUAL = "=";
	String EQUAL_PLACEHOLDER = "=?";
	String NOT_EQUAL_PLACEHOLDER = "!=?";
	String AND = " and ";
	String IN_PLACEHOLDER = " in (%s)";

	String INSERT_TPL = "insert into %s (%s) values (%s)";
	String UPDATE_TPL = "update %s set %s where %s";
	String DELETE_TPL = "delete from %s where %s";
	String SELECT_TPL = "select * from %s where %s";
	String GET_TPL = "select %s from %s where %s";
	String COUNT_TPL = "select count(*) from %s where %s";

	SqlItem sql(SqlParam param);

	default void checkMap(Map<String, ?> map) {
		if (map == null || map.isEmpty()) {
			throw new JdbcException("参数不能为空");
		}
	}

	default ColumnConfig getColumn(String name, Map<String, ColumnConfig> columnMap)
	{
		var column = columnMap.get(name);
		if (column == null) {
			throw new JdbcException("字段 [" + name + "] 不存在");
		}

		return column;
	}

	default String getColumnString(String name, Map<String, IdConfig> idMap, Map<String,
			ColumnConfig> columnMap, VersionConfig version) {
		if (columnMap != null && columnMap.get(name) != null) {
			return columnMap.get(name).getEscapeColumnName();
		}
		if (idMap != null && idMap.get(name) != null) {
			return idMap.get(name).getEscapeColumnName();
		}
		if (version != null && version.getFieldName().equals(name)) {
			return version.getEscapeColumnName();
		}
		throw new JdbcException("字段 [" + name + "] 不存在");
	}

}
