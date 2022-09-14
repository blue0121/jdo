package io.jutil.jdo.core.parser;

import io.jutil.jdo.core.reflect.ClassFieldOperation;

/**
 * 字段元数据
 *
 * @author Jin Zheng
 * @since 2022-05-11
 */
public interface FieldMetadata {

	/**
	 * 字段名称
	 *
	 * @return
	 */
	String getFieldName();

	/**
	 * 数据表列名
	 *
	 * @return
	 */
	String getColumnName();

	/**
	 * Bean字段操作
	 *
	 * @return
	 */
	ClassFieldOperation getFieldOperation();

}
