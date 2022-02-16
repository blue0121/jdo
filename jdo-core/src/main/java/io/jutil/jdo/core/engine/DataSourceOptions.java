package io.jutil.jdo.core.engine;

import io.jutil.jdo.internal.core.util.AssertUtil;
import lombok.Getter;

/**
 * @author Jin Zheng
 * @since 2022-02-16
 */
@Getter
public class DataSourceOptions {
	private String url;
	private String username;
	private String password;

	private int initialSize = 1;
	private int minIdle = 1;
	private int maxActive = 20;
	private int maxWait = 6000;

	private int timeBetweenEvictionRunsMillis = 60000;
	private int minEvictableIdleTimeMillis = 600000;
	private int maxEvictableIdleTimeMillis = 900000;

	private String validationQuery = "select 1";
	private boolean testWhileIdle = true;
	private boolean testOnBorrow = false;
	private boolean testOnReturn = false;
	private boolean keepAlive = true;
	private boolean poolPreparedStatements = false;
	private boolean asyncInit = true;

	private String filters = "stat";

	public DataSourceOptions() {
	}

	/**
	 * 检查验证配置
	 */
	public void check() {
		AssertUtil.notEmpty(url, "DataSource url");
	}

	public DataSourceOptions setUrl(String url) {
		this.url = url;
		return this;
	}

	public DataSourceOptions setUsername(String username) {
		this.username = username;
		return this;
	}

	public DataSourceOptions setPassword(String password) {
		this.password = password;
		return this;
	}

	public DataSourceOptions setInitialSize(int initialSize) {
		this.initialSize = initialSize;
		return this;
	}

	public DataSourceOptions setMinIdle(int minIdle) {
		this.minIdle = minIdle;
		return this;
	}

	public DataSourceOptions setMaxActive(int maxActive) {
		this.maxActive = maxActive;
		return this;
	}

	public DataSourceOptions setMaxWait(int maxWait) {
		this.maxWait = maxWait;
		return this;
	}

	public DataSourceOptions setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
		return this;
	}

	public DataSourceOptions setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
		return this;
	}

	public DataSourceOptions setMaxEvictableIdleTimeMillis(int maxEvictableIdleTimeMillis) {
		this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
		return this;
	}

	public DataSourceOptions setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
		return this;
	}

	public DataSourceOptions setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
		return this;
	}

	public DataSourceOptions setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
		return this;
	}

	public DataSourceOptions setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
		return this;
	}

	public DataSourceOptions setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	public DataSourceOptions setPoolPreparedStatements(boolean poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
		return this;
	}

	public DataSourceOptions setAsyncInit(boolean asyncInit) {
		this.asyncInit = asyncInit;
		return this;
	}

	public DataSourceOptions setFilters(String filters) {
		this.filters = filters;
		return this;
	}
}
