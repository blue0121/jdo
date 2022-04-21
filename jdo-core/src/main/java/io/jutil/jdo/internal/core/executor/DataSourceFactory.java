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
		var ds = new DruidDataSource();
		ds.setUrl(options.getUrl());
		ds.setUsername(options.getUsername());
		ds.setPassword(options.getPassword());

		ds.setInitialSize(options.getInitialSize());
		ds.setMinIdle(options.getMinIdle());
		ds.setMaxActive(options.getMaxActive());
		ds.setMaxWait(options.getMaxWait());

		ds.setTimeBetweenEvictionRunsMillis(options.getTimeBetweenEvictionRunsMillis());
		ds.setMinEvictableIdleTimeMillis(options.getMinEvictableIdleTimeMillis());
		ds.setMaxEvictableIdleTimeMillis(options.getMaxEvictableIdleTimeMillis());

		ds.setValidationQuery(options.getValidationQuery());
		ds.setTestWhileIdle(options.isTestWhileIdle());
		ds.setTestOnBorrow(options.isTestOnBorrow());
		ds.setTestOnReturn(options.isTestOnReturn());
		ds.setKeepAlive(options.isKeepAlive());
		ds.setPoolPreparedStatements(options.isPoolPreparedStatements());
		ds.setAsyncInit(options.isAsyncInit());

		try {
			ds.setFilters(options.getFilters());
			ds.init();
		} catch (SQLException e) {
			logger.error("错误, ", e);
		}
		logger.info("Druid 数据源初始化, URL: {}", options.getUrl());
		return ds;
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
