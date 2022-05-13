package io.jutil.jdo.core.parser2;

import io.jutil.jdo.core.reflect2.ClassFieldOperation;

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
	 * 转义后的数据表列名
	 *
	 * @return
	 */
	String getEscapeColumnName();

	/**
	 * Bean字段操作
	 *
	 * @return
	 */
	ClassFieldOperation getFieldOperation();

}
