package io.jutil.jdo.core.reflect;

import io.jutil.jdo.internal.core.reflect.DefaultMethodSignature;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 方法签名
 *
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface MethodSignature extends NameOperation {

	/**
	 * 创建方法签名对象
	 *
	 * @param name
	 * @param parameterTypes
	 * @return
	 */
	static MethodSignature create(String name, Class<?>... parameterTypes) {
		return new DefaultMethodSignature(name, parameterTypes);
	}

	/**
	 * 创建方法签名对象
	 *
	 * @param name
	 * @param parameterTypes
	 * @return
	 */
	static MethodSignature create(String name, Collection<Class<?>> parameterTypes) {
		return new DefaultMethodSignature(name, parameterTypes);
	}

	/**
	 * 创建方法签名对象
	 *
	 * @param method
	 * @return
	 */
	static MethodSignature create(Method method) {
		return new DefaultMethodSignature(method);
	}

	/**
	 * 方法参数类型
	 *
	 * @return
	 */
	Class<?>[] getParameterTypes();

}
