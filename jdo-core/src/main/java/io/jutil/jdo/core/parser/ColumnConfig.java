package io.jutil.jdo.core.parser;

/**
 * 普通字段配置
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
public interface ColumnConfig extends FieldConfig {
	/**
	 * 是否强制插入空值
	 */
	boolean isMustInsert();

	/**
	 * 是否强制更新空值
	 */
	boolean isMustUpdate();

}
