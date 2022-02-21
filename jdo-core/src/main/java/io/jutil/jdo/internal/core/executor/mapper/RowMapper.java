package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 行映射
 *
 * @author Jin Zheng
 * @since 2022-02-21
 */
public interface RowMapper<T> {

	int ONE = 1;

	/**
	 * 映射类型
	 */
	Class<T> getType();

	/**
	 * ResultSet => 对象实例
	 */
	T mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException;
}
