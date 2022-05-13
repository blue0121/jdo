package io.jutil.jdo.core.parser2;

/**
 * 普通字段元数据
 *
 * @author Jin Zheng
 * @since 2022-05-11
 */
public interface ColumnMetadata extends FieldMetadata {

	/**
	 * 是否强制插入空值
	 *
	 * @return
	 */
	boolean isMustInsert();

	/**
	 * 是否强制更新空值
	 *
	 * @return
	 */
	boolean isMustUpdate();
}
