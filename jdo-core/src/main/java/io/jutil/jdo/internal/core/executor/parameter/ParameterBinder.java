package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public interface ParameterBinder<T> {

	/**
	 * 绑定类型
	 *
	 * @return
	 */
	Class<T> getType();

	/**
	 * PreparedStatement 参数绑定
	 *
	 * @param context
	 * @throws SQLException
	 */
	default void bind(BindContext<T> context) throws SQLException {
	}

	/**
	 * 从ResultSet获取值
	 *
	 * @param context
	 * @return
	 * @throws SQLException
	 */

	default T fetch(FetchContext context) throws SQLException {
		return null;
	}

	/**
	 * PreparedStatement 参数绑定
	 *
	 * @param pstmt
	 * @param i
	 * @param val
	 * @throws SQLException
	 */
	void bind(PreparedStatement pstmt, int i, T val) throws SQLException;


	/**
	 * 从 ResultSet 获取值
	 *
	 * @param rsmd
	 * @param rs
	 * @param i
	 * @return
	 * @throws SQLException
	 */
	T fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException;

}
