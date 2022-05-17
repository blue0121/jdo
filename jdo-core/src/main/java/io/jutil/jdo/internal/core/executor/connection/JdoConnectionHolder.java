package io.jutil.jdo.internal.core.executor.connection;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.util.JdbcUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Jin Zheng
 * @since 2022-05-17
 */
public class JdoConnectionHolder implements ConnectionHolder {
	private final ThreadLocal<Boolean> tlAutoCommit;
	private final ThreadLocal<Connection> tlConnection;

	public JdoConnectionHolder(DataSource dataSource) {
		this.tlAutoCommit = ThreadLocal.withInitial(() -> true);
		this.tlConnection = ThreadLocal.withInitial(() -> {
			try {
				boolean autoCommit = tlAutoCommit.get();
				var conn = dataSource.getConnection();
				if (!autoCommit) {
					conn.setAutoCommit(false);
				}
				return conn;
			} catch (SQLException e) {
				throw new JdbcException(e);
			}
		});
	}

	@Override
	public Connection getConnection() {
		return tlConnection.get();
	}

	@Override
	public void close(ResultSet rs, Statement stmt, Connection conn) {
		var autoCommit = this.tlAutoCommit.get();
		if (autoCommit) {
			JdbcUtil.close(rs, stmt, conn);
			tlConnection.remove();
		} else {
			JdbcUtil.close(rs, stmt, null);
		}
	}

	public void setAutoCommit(boolean autoCommit) {
		this.tlAutoCommit.set(autoCommit);
		try {
			this.tlConnection.get().setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new JdbcException(e);
		}
	}
}
