package io.jutil.jdo.internal.core.executor;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.executor.connection.ConnectionHolder;
import io.jutil.jdo.internal.core.executor.parameter.ParameterBinderFacade;
import io.jutil.jdo.internal.core.parser.ParserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-17
 */
public class SqlExecutor {
	private static Logger logger = LoggerFactory.getLogger(SqlExecutor.class);

	private final ConnectionHolder connectionHolder;
	private final ParameterBinderFacade binderFacade;

	public SqlExecutor(ConnectionHolder connectionHolder, ParserFacade parserFacade) {
		this.connectionHolder = connectionHolder;
		this.binderFacade = new ParameterBinderFacade(parserFacade);
	}

	public <T> List<T> query(Class<T> clazz, String sql, List<?> paramList) {
		this.logParam(sql, paramList);
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = connectionHolder.getConnection();
			pstmt = conn.prepareStatement(sql);
			binderFacade.bind(pstmt, paramList);
			rs = pstmt.executeQuery();
			return binderFacade.fetch(rs, clazz);
		} catch (Exception e) {
			throw new JdbcException(e);
		} finally {
			connectionHolder.close(rs, pstmt, conn);
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
			conn = connectionHolder.getConnection();
			pstmt = this.createPreparedStatement(conn, sql, holder);
			binderFacade.bind(pstmt, paramList);
			int count = pstmt.executeUpdate();
			this.handleKeyHolder(pstmt, holder);
			return count;
		} catch (Exception e) {
			throw new JdbcException(e);
		} finally {
			connectionHolder.close(null, pstmt, conn);
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
			conn = connectionHolder.getConnection();
			pstmt = this.createPreparedStatement(conn, sql, holder);
			for (var paramList : batchList) {
				this.logParam(null, paramList);
				binderFacade.bind(pstmt, paramList);
				pstmt.addBatch();
			}
			int[] count = pstmt.executeBatch();
			this.handleKeyHolder(pstmt, holder);
			return count;
		} catch (Exception e) {
			throw new JdbcException(e);
		} finally {
			connectionHolder.close(null, pstmt, conn);
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
			conn = connectionHolder.getConnection();
			stmt = conn.createStatement();
			return stmt.executeUpdate(sql);
		} catch (Exception e) {
			throw new JdbcException(e);
		} finally {
			connectionHolder.close(null, stmt, conn);
		}
	}
}
