package io.jutil.jdo.internal.core.executor;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.executor.mapper.RowMapperFactory;
import io.jutil.jdo.internal.core.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class ConnectionFactory {
	private static Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

	private final DataSource dataSource;
	private final RowMapperFactory rowMapperFactory;
	private final ThreadLocal<Boolean> tlAutoCommit;
	private final ThreadLocal<Connection> tlConnection;

	public ConnectionFactory(DataSource dataSource, RowMapperFactory rowMapperFactory) {
		this.dataSource = dataSource;
		this.rowMapperFactory = rowMapperFactory;
		this.tlAutoCommit = ThreadLocal.withInitial(() -> true);
		this.tlConnection = ThreadLocal.withInitial(() -> {
			try {
				var conn = dataSource.getConnection();
				//System.out.println("init>>" + conn);
				boolean autoCommit = tlAutoCommit.get().booleanValue();
				if (!autoCommit) {
					conn.setAutoCommit(false);
				}
				return conn;
			} catch (SQLException e) {
				throw new JdbcException(e);
			}
		});
	}

	public void setAutoCommit(boolean autoCommit) {
		this.tlAutoCommit.set(autoCommit);
	}

    public Connection getConnection() throws SQLException {
        var conn = tlConnection.get();
	    //System.out.println("get>>" + conn);
		return conn;
    }

	public void close(ResultSet rs, Statement stmt, Connection conn) {
		var autoCommit = this.tlAutoCommit.get().booleanValue();
		if (autoCommit) {
			JdbcUtil.close(rs, stmt, conn);
			tlConnection.remove();
		} else {
			JdbcUtil.close(rs, stmt, null);
		}
	}

    public void destroy() {
		try {
			var conn = this.getConnection();
			JdbcUtil.close(conn);
			tlConnection.remove();
		} catch (SQLException e) {
			logger.warn("销毁数据库连接错误, ", e);
		}
		tlAutoCommit.remove();
    }

	public <T> List<T> query(Object config, String sql, List<?> paramList) {
		this.logParam(sql, paramList);
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = this.getConnection();
			pstmt = conn.prepareStatement(sql);
			JdbcUtil.setParams(pstmt, paramList);
			rs = pstmt.executeQuery();
			return rowMapperFactory.getObjectList(config, rs);
		} catch (Exception e) {
			throw new JdbcException(e);
		} finally {
			this.close(rs, pstmt, conn);
		}
	}

	public int execute(String sql, List<?> paramList) {
		return this.execute(sql, paramList, null);
	}

	public int execute(String sql, List<?> paramList, KeyHolder holder) {
		this.logParam(sql, paramList);
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = this.getConnection();
			pstmt = this.createPreparedStatement(conn, sql, holder);
			JdbcUtil.setParams(pstmt, paramList);
			int count = pstmt.executeUpdate();
			this.handleKeyHolder(pstmt, holder);
			return count;
		} catch (Exception e) {
			throw new JdbcException(e);
		} finally {
			this.close(null, pstmt, conn);
		}
	}

	public int[] executeBatch(String sql, List<List<?>> batchList) {
		return executeBatch(sql, batchList, null);
	}

	public int[] executeBatch(String sql, List<List<?>> batchList, KeyHolder holder) {
		this.logParam(sql, null);
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = this.getConnection();
			pstmt = this.createPreparedStatement(conn, sql, holder);
			for (var paramList : batchList) {
				this.logParam(null, paramList);
				JdbcUtil.setParams(pstmt, paramList);
				pstmt.addBatch();
			}
			int[] count = pstmt.executeBatch();
			this.handleKeyHolder(pstmt, holder);
			return count;
		} catch (Exception e) {
			throw new JdbcException(e);
		} finally {
			this.close(null, pstmt, conn);
		}
	}

	private void logParam(String sql, List<?> paramList) {
		if (logger.isDebugEnabled()) {
			if (sql == null || sql.isEmpty()) {
				logger.debug("SQL param: {}", paramList);
			} else if (paramList == null) {
				logger.debug("SQL: {}", sql);
			} else {
				logger.debug("SQL: {}, param: {}", sql, paramList);
			}
		}
	}

	private void handleKeyHolder(Statement stmt, KeyHolder holder) throws SQLException {
		if (holder == null) {
			return;
		}
		var rs = stmt.getGeneratedKeys();
		holder.mapRow(rs);
	}

	private PreparedStatement createPreparedStatement(Connection conn, String sql, KeyHolder holder)
			throws SQLException {
		if (holder == null) {
			return conn.prepareStatement(sql);
		}
		return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	}

	public int execute(String sql) {
		this.logParam(sql, null);
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			return stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JdbcException(e);
		} finally {
			this.close(null, stmt, conn);
		}
	}

}
