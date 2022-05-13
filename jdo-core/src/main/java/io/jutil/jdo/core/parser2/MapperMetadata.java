package io.jutil.jdo.core.parser2;

import io.jutil.jdo.core.reflect2.ClassOperation;

import java.util.Map;

/**
 * 映射元数据
 *
 * @author Jin Zheng
 * @since 2022-05-11
 */
public interface MapperMetadata {

	/**
	 * 元数据类型
	 *
	 * @return
	 */
	MetadataType getMetadataType();

	/**
	 * 目标类型
	 *
	 * @return
	 */
	Class<?> getTargetClass();

	/**
	 * Class类操作
	 *
	 * @return
	 */
	ClassOperation getClassOperation();

	/**
	 * 所有普通字段元数据
	 *
	 * @return Map<字段名, 普通字段元数据>
	 */
	Map<String, ColumnMetadata> getColumnMap();

}
