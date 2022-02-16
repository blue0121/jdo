package io.jutil.jdo.core.parser;

/**
 * 版本配置
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
public interface VersionConfig extends FieldConfig {

	/**
	 * 是否强制
	 */
	boolean isForce();

	/**
	 * 默认值
	 *
	 * @return
	 */
	int getDefaultValue();

}
