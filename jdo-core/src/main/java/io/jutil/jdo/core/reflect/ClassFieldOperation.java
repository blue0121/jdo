package io.jutil.jdo.core.reflect;

/**
 * 类字段操作
 *
 * @author Jin Zheng
 * @since 2022-04-28
 */
public interface ClassFieldOperation extends FieldOperation {
	/**
	 * 类操作
	 *
	 * @return
	 */
	ClassOperation getClassOperation();

	/**
	 * 关联Getter方法
	 *
	 * @return
	 */
	ClassMethodOperation getGetterMethod();

	/**
	 * 关联Setter方法
	 *
	 * @return
	 */
	ClassMethodOperation getSetterMethod();

	/**
	 * 获取字段值步骤：
	 * 1. 调用Getter方法<br/>
	 * 2. field.setAccessible(true) & field.get(target)
	 *
	 * @param target
	 * @return
	 */
	Object getFieldValue(Object target);

	/**
	 * 获取字段值步骤：
	 * 1. 调用Setter方法<br/>
	 * 2. field.setAccessible(true) & field.set(target, value)
	 *
	 * @param target
	 * @return
	 */
	boolean setFieldValue(Object target, Object value);

}
