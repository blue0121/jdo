package io.jutil.jdo.internal.spring.plugin;

import io.jutil.jdo.core.plugin.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Jin Zheng
 * @since 2022-05-19
 */
public class SpringConnectionHolder implements ConnectionHolder {
	private final DataSource dataSource;

	public SpringConnectionHolder(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
    public Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public void close(ResultSet rs, Statement stmt, Connection conn) {
	    JdbcUtils.closeResultSet(rs);
	    JdbcUtils.closeStatement(stmt);
		DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
