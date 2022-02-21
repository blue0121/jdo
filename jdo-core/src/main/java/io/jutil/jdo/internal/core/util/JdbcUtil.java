package io.jutil.jdo.internal.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class JdbcUtil {
	private static Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

	private JdbcUtil() {
	}

	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		close(rs);
		close(stmt);
		close(conn);
	}

	public static void close(AutoCloseable res) {
		if (res != null) {
			try {
				res.close();
			} catch (Exception e) {
				logger.warn("资源关闭错误, ", e);
			}
		}
	}

	public static void setParams(PreparedStatement pstmt, Collection<?> list) throws SQLException {
		if (list == null || list.isEmpty()) {
			return;
		}

		int i = 1;
		for (Object obj : list) {
			if (obj == null) {
				pstmt.setObject(i, null);
				continue;
			}

			pstmt.setObject(i, obj);
			i++;
		}
	}

}
