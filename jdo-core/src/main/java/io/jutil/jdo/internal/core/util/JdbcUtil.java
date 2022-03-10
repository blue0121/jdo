package io.jutil.jdo.internal.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

}
