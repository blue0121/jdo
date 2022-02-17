package io.jutil.jdo.core.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法
 *
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface BeanMethod extends ExecutableOperation {

	/**
	 * 目标对象
	 *
	 * @return
	 */
	Object getTarget();

	/**
	 * 返回类型
	 *
	 * @return
	 */
	Class<?> getReturnType();

	/**
	 * 方法
	 *
	 * @return
	 */
	Method getMethod();

	/**
	 * 方法签名
	 *
	 * @return
	 */
	MethodSignature getMethodSignature();

	/**
	 * 调用方法，并抛出异常
	 *
	 * @param target
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	Object invoke(Object target, Object... args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;

	/**
	 * 调用方法，如有异常则返回null
	 *
	 * @param target
	 * @param args
	 * @return
	 */
	Object invokeQuietly(Object target, Object... args);

	/**
	 * JavaBean所代表的字段名
	 *
	 * @return
	 */
	String getRepresentField();

	/**
	 * 是否Setter方法
	 *
	 * @return
	 */
	boolean isSetter();

	/**
	 * 是否Getter方法
	 *
	 * @return
	 */
	boolean isGetter();

}
