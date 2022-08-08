package io.jutil.jdo.internal.core.executor.parameter;

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
	void bind(BindContext<T> context) throws SQLException;

	/**
	 * 从ResultSet获取值
	 *
	 * @param context
	 * @return
	 * @throws SQLException
	 */

	T fetch(FetchContext context) throws SQLException;

}
