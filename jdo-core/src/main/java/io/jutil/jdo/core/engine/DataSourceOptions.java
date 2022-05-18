package io.jutil.jdo.core.engine;

import io.jutil.jdo.core.plugin.DataSourceHolder;
import io.jutil.jdo.internal.core.plugin.JdoDataSourceHolder;
import io.jutil.jdo.internal.core.util.AssertUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Jin Zheng
 * @since 2022-02-16
 */
@Getter
@NoArgsConstructor
public class DataSourceOptions {
	private String url;
	private String username;
	private String password;

	private int minimumIdle = 10;
	private int maximumPoolSize = 10;
	private String poolName;

	private boolean autoCommit = true;
	private int connectionTimeout = 30_000;
	private int idleTimeout = 600_000;
	private int keepaliveTime = 0;
	private int maxLifetime = 1_800_000;

	private volatile DataSourceHolder holder;

	public DataSourceHolder getDataSourceHolder() {
		AssertUtil.notEmpty(url, "DataSource url");
		if (holder == null) {
			synchronized (this) {
				if (holder == null) {
					this.holder = new JdoDataSourceHolder(this);
				}
			}
		}
		return holder;
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

	public DataSourceOptions setMinimumIdle(int minimumIdle) {
		this.minimumIdle = minimumIdle;
		return this;
	}

	public DataSourceOptions setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
		return this;
	}

	public DataSourceOptions setPoolName(String poolName) {
		this.poolName = poolName;
		return this;
	}

	public DataSourceOptions setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
		return this;
	}

	public DataSourceOptions setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	public DataSourceOptions setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
		return this;
	}

	public DataSourceOptions setKeepaliveTime(int keepaliveTime) {
		this.keepaliveTime = keepaliveTime;
		return this;
	}

	public DataSourceOptions setMaxLifetime(int maxLifetime) {
		this.maxLifetime = maxLifetime;
		return this;
	}

}
