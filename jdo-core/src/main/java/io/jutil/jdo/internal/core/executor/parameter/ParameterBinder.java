package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public interface ParameterBinder<T> {

	/**
	 * 绑定类型
	 * @return
	 */
	Class<T> getType();

	/**
	 * PreparedStatement 参数绑定
	 *
	 * @param pstmt
	 * @param i
	 * @param val
	 * @throws SQLException
	 */
	void bind(PreparedStatement pstmt, int i, T val) throws SQLException;

}
