package io.jutil.jdo.core.parser;

import blue.base.core.reflect.BeanField;

/**
 * 字段配置
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
public interface FieldConfig {
	/**
	 * 检查验证配置
	 */
	void check();

	/**
	 * 字段名称
	 */
	String getFieldName();

	/**
	 * 数据表列名
	 */
	String getColumnName();

	/**
	 * 转义后的数据表列名
	 */
	String getEscapeColumnName();

	/**
	 * Bean字段
	 */
	BeanField getBeanField();
}
