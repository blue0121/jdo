package io.jutil.jdo.core.reflect2;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 类操作
 *
 * @author Jin Zheng
 * @since 2022-04-28
 */
public interface ClassOperation {

	/**
	 * 获取目标类型
	 *
	 * @return
	 */
	Class<?> getTargetClass();

	/**
	 * 根据参数类型获取构造方法
	 *
	 * @param types
	 * @return
	 */
	ClassConstructorOperation getConstructor(Class<?>... types);

	/**
	 * 实例化对象，并抛出异常
	 *
	 * @param args
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	Object newInstance(Object... args) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;

	/**
	 * 实例化对象，如有异常则返回null
	 *
	 * @param args
	 * @return
	 */
	Object newInstanceQuietly(Object... args);

	/**
	 * 获取所有方法
	 *
	 * @return
	 */
	List<ClassMethodOperation> getAllMethods();

	/**
	 * 根据方法签名获取方法
	 *
	 * @param methodSignature
	 * @return
	 */
	ClassMethodOperation getMethod(MethodSignature methodSignature);

	/**
	 * 获取所有字段
	 *
	 * @return
	 */
	Map<String, ClassFieldOperation> getAllFields();

	/**
	 * 根据字段名获取字段
	 *
	 * @param fieldName
	 * @return
	 */
	ClassFieldOperation getField(String fieldName);

	/**
	 * 根据对象获取所有字段值
	 *
	 * @param target object
	 * @return fieldName => fieldValue
	 */
	Map<String, Object> getAllFieldValues(Object target);

}
