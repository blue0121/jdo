package io.jutil.jdo.core.engine;

import io.jutil.jdo.core.plugin.ConnectionHolder;
import io.jutil.jdo.internal.core.engine.DefaultJdoBuilder;

import javax.sql.DataSource;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public interface JdoBuilder {
	/**
	 * 创建JdoBuilder实例
	 *
	 * @return
	 */
	static JdoBuilder create() {
		return new DefaultJdoBuilder();
	}

	/**
	 * 生成Jdo实例
	 *
	 * @return
	 */
	Jdo build();

	/**
	 * 设置 DataSource
	 *
	 * @param dataSource
	 * @return
	 */
	JdoBuilder setDataSource(DataSource dataSource);

	/**
	 * 添加类解析, 比如@Entity, @Mapper
	 *
	 * @param clazzes
	 * @return
	 */
	JdoBuilder addClazz(Class<?>... clazzes);

	/**
	 * 添加扫描包
	 *
	 * @param pkgs
	 * @return
	 */
	JdoBuilder addScanPackages(String... pkgs);

	/**
	 * 设置数据库连接占位
	 *
	 * @param holder
	 * @return
	 */
	JdoBuilder setConnectionHolder(ConnectionHolder holder);

	/**
	 * 设置数据库初始化SQL
	 *
	 * @param sql
	 * @return
	 */
	JdoBuilder setInitSql(String sql);

}
