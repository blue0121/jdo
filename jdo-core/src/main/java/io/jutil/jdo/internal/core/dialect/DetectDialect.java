package io.jutil.jdo.internal.core.dialect;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class DetectDialect {
	private static Logger logger = LoggerFactory.getLogger(DetectDialect.class);

	private DetectDialect() {
	}

	public static Dialect dialect(DataSource dataSource) {
		try (Connection conn = dataSource.getConnection()) {
			DatabaseMetaData meta = conn.getMetaData();
			String productName = meta.getDatabaseProductName();
			String productVersion = meta.getDatabaseProductVersion();
			String driverName = meta.getDriverName();
			String driverVersion = meta.getDriverVersion();
			String url = meta.getURL();
			String user = meta.getUserName();
			logger.info("数据库: {}[{}], 驱动: {}[{}], url: {}, 用户: {}", productName, productVersion,
					driverName, driverVersion, url, user);

			return dialect(url);
		} catch (SQLException e) {
			throw new JdbcException(e);
		}
	}

	private static Dialect dialect(String url) {
		String jdbcType = StringUtil.getJdbcType(url);
		Dialect dialect = switch (jdbcType) {
			case MySQLDialect.PROTOCOL -> new MySQLDialect();
			case PostgreSQLDialect.PROTOCOL -> new PostgreSQLDialect();
			case OracleDialect.PROTOCOL -> new OracleDialect();
			case HyperSQLDialect.PROTOCOL -> new HyperSQLDialect();
			default -> null;
		};

		if (dialect != null) {
			logger.info("数据库方言: {}", dialect.getClass().getName());
		}

		return dialect;
	}

}
