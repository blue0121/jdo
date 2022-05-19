package io.jutil.jdo.internal.core.engine;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jutil.jdo.core.engine.DataSourceOptions;

import javax.sql.DataSource;

/**
 * @author Jin Zheng
 * @since 2022-05-18
 */
public class JdoDataSourceHolder {
	private final HikariDataSource dataSource;

	public JdoDataSourceHolder(DataSourceOptions options) {
		this.dataSource = this.initDataSource(options);
	}

	private HikariDataSource initDataSource(DataSourceOptions options) {
		var config = new HikariConfig();
		config.setJdbcUrl(options.getUrl());
		config.setUsername(options.getUsername());
		config.setPassword(options.getPassword());

		config.setMinimumIdle(options.getMinimumIdle());
		config.setMaximumPoolSize(options.getMaximumPoolSize());
		config.setPoolName(options.getPoolName());

		config.setAutoCommit(options.isAutoCommit());
		config.setConnectionTimeout(options.getConnectionTimeout());
		config.setIdleTimeout(options.getIdleTimeout());
		config.setKeepaliveTime(options.getKeepaliveTime());
		config.setMaxLifetime(options.getMaxLifetime());

		return new HikariDataSource(config);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

}
