package io.jutil.jdo.core.plugin;

import javax.sql.DataSource;

/**
 * @author Jin Zheng
 * @since 2022-05-18
 */
public interface DataSourceHolder {

	/**
	 * 数据源
	 *
	 * @return
	 */
	DataSource getDataSource();

	/**
	 * 关闭数据库连接
	 */
	void close();

}
