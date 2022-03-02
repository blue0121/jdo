package io.jutil.jdo.core.engine;

import io.jutil.jdo.internal.core.engine.DefaultJdoBuilder;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public interface JdoBuilder {
	/**
	 * 创建JdoBuilder实例
	 */
	static JdoBuilder create() {
		return new DefaultJdoBuilder();
	}

	/**
	 * 生成Jdo实例
	 */
	Jdo build();

	/**
	 * 设置 DataSourceOptions
	 */
	JdoBuilder setDataSourceOptions(DataSourceOptions options);

	/**
	 * 添加类解析, 比如@Entity, @Mapper
	 */
	JdoBuilder addClazz(Class<?>...clazzes);

	/**
	 * 添加扫描包
	 */
	JdoBuilder addScanPackages(String...pkgs);
}
