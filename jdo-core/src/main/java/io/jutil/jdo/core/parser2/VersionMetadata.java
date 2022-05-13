package io.jutil.jdo.core.parser2;

/**
 * 版本元数据
 *
 * @author Jin Zheng
 * @since 2022-05-11
 */
public interface VersionMetadata extends FieldMetadata {


	/**
	 * 是否强制
	 *
	 * @return
	 */
	boolean isForce();

	/**
	 * 默认值
	 *
	 * @return
	 */
	int getDefaultValue();
}
