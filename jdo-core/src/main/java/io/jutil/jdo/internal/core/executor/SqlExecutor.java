package io.jutil.jdo.internal.core.executor;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.plugin.ConnectionHolder;
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
	private static Logger sqlLogger = LoggerFactory.getLogger("SQL");

	private final ConnectionHolder connectionHolder;
	private final ParameterBinderFacade binderFacade;

	public SqlExecutor(ConnectionHolder connectionHolder, ParserFacade parserFacade) {
		this.connectionHolder = connectionHolder;
		this.binderFacade = new ParameterBinderFacade(parserFacade);
	}

	public <T> List<T> query(Class<T> clazz, ExecuteContext context) {
		this.logParam(context.getSql(), context.getParameterList());
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = connectionHolder.getConnection();
			pstmt = conn.prepareStatement(context.getSql());
			binderFacade.bind(pstmt, context.getParameterList());
			rs = pstmt.executeQuery();
			return binderFacade.fetch(rs, clazz);
		} catch (Exception e) {
			throw new JdbcException(e);
		} finally {
			connectionHolder.close(rs, pstmt, conn);
		}
	}

	public int execute(ExecuteContext context) {
		return this.execute(context, null);
	}

	public int execute(ExecuteContext context, KeyHolder holder) {
		this.logParam(context.getSql(), context.getParameterList());
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connectionHolder.getConnection();
			pstmt = this.createPreparedStatement(conn, context.getSql(), holder);
			binderFacade.bind(pstmt, context.getParameterList());
			int count = pstmt.executeUpdate();
			this.handleKeyHolder(pstmt, holder);
			return count;
		} catch (Exception e) {
			throw new JdbcException(e);
		} finally {
			connectionHolder.close(null, pstmt, conn);
		}
	}

	public int[] executeBatch(ExecuteContext context) {
		return executeBatch(context, null);
	}

	public int[] executeBatch(ExecuteContext context, KeyHolder holder) {
		this.logParam(context.getSql(), null);
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connectionHolder.getConnection();
			pstmt = this.createPreparedStatement(conn, context.getSql(), holder);
			for (var paramList : context.getBatchParameterList()) {
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
				sqlLogger.info("parameters: {}", paramList);
			} else if (paramList == null) {
				sqlLogger.info("{}", sql);
			} else {
				sqlLogger.info("{}, parameters: {}", sql, paramList);
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
