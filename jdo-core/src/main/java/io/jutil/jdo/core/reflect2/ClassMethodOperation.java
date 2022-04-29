package io.jutil.jdo.core.reflect2;

import java.lang.reflect.InvocationTargetException;

/**
 * 类方法操作
 *
 * @author Jin Zheng
 * @since 2022-04-28
 */
public interface ClassMethodOperation extends MethodOperation {
	/**
	 * 类操作
	 *
	 * @return
	 */
	ClassOperation getClassOperation();

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

}
