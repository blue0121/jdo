package io.jutil.jdo.core.reflect;

import java.lang.reflect.InvocationTargetException;

/**
 * 构造方法操作
 *
 * @author Jin Zheng
 * @since 2022-04-28
 */
public interface ClassConstructorOperation extends ExecutableOperation {
	/**
	 * 类操作
	 *
	 * @return
	 */
	ClassOperation getClassOperation();

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
}
