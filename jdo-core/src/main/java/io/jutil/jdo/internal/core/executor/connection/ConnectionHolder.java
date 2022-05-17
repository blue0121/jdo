package io.jutil.jdo.internal.core.executor.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Jin Zheng
 * @since 2022-05-17
 */
public interface ConnectionHolder {

	/**
	 * 数据库连接
	 *
	 * @return
	 * @throws SQLException
	 */
	Connection getConnection();

	/**
	 * 关闭资源
	 *
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	void close(ResultSet rs, Statement stmt, Connection conn);

}
