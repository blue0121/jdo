package io.jutil.jdo.internal.core.plugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jutil.jdo.core.engine.DataSourceOptions;
import io.jutil.jdo.core.plugin.DataSourceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * @author Jin Zheng
 * @since 2022-05-18
 */
public class JdoDataSourceHolder implements DataSourceHolder {
	private static Logger logger = LoggerFactory.getLogger(JdoDataSourceHolder.class);

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

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

    @Override
    public void close() {
		if (dataSource != null && !dataSource.isClosed()) {
			dataSource.close();
			logger.info("Hikari 数据源关闭");
		}
    }
}
