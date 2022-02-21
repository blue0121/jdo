package io.jutil.jdo.internal.core.executor;

import com.alibaba.druid.pool.DruidDataSource;
import io.jutil.jdo.core.engine.DataSourceOptions;
import io.jutil.jdo.internal.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class DataSourceFactory implements AutoCloseable {
    private static Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

	private final DataSourceOptions options;
    private final DruidDataSource dataSource;

	public DataSourceFactory(DataSourceOptions options) {
		AssertUtil.notNull(options, "数据源配置");
		this.options = options;
		this.dataSource = this.initDataSource();
	}

	private DruidDataSource initDataSource() {
		var dataSource = new DruidDataSource();
		dataSource.setUrl(options.getUrl());
		dataSource.setUsername(options.getUsername());
		dataSource.setPassword(options.getPassword());

		dataSource.setInitialSize(options.getInitialSize());
		dataSource.setMinIdle(options.getMinIdle());
		dataSource.setMaxActive(options.getMaxActive());
		dataSource.setMaxWait(options.getMaxWait());

		dataSource.setTimeBetweenEvictionRunsMillis(options.getTimeBetweenEvictionRunsMillis());
		dataSource.setMinEvictableIdleTimeMillis(options.getMinEvictableIdleTimeMillis());
		dataSource.setMaxEvictableIdleTimeMillis(options.getMaxEvictableIdleTimeMillis());

		dataSource.setValidationQuery(options.getValidationQuery());
		dataSource.setTestWhileIdle(options.isTestWhileIdle());
		dataSource.setTestOnBorrow(options.isTestOnBorrow());
		dataSource.setTestOnReturn(options.isTestOnReturn());
		dataSource.setKeepAlive(options.isKeepAlive());
		dataSource.setPoolPreparedStatements(options.isPoolPreparedStatements());
		dataSource.setAsyncInit(options.isAsyncInit());

		try {
			dataSource.setFilters(options.getFilters());
			dataSource.init();
		} catch (SQLException e) {
			logger.error("错误, ", e);
		}
		logger.info("Druid 数据源初始化, URL: {}", options.getUrl());
		return dataSource;
	}

	public DataSource getDateSource() {
		return dataSource;
	}

	@Override
	public void close() {
		if (dataSource != null) {
			dataSource.close();
			logger.info("Druid 数据源关闭");
		}
	}
}
