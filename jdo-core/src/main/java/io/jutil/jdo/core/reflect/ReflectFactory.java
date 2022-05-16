package io.jutil.jdo.core.reflect;

import io.jutil.jdo.internal.core.reflect.DefaultClassOperation;
import io.jutil.jdo.internal.core.reflect.DefaultMethodSignature;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Jin Zheng
 * @since 2022-05-05
 */
public class ReflectFactory {
	private static ConcurrentMap<Class<?>, ClassOperation> classOperationMap = new ConcurrentHashMap<>();

	private ReflectFactory() {
	}

	/**
	 * 根据目标类型获取缓存中的JavaBean对象，不存在则创建
	 *
	 * @param targetClass
	 * @return
	 */
	public static ClassOperation getClassOperation(Class<?> targetClass) {
		return classOperationMap.computeIfAbsent(targetClass, DefaultClassOperation::new);
	}

	/**
	 * 创建方法签名对象
	 *
	 * @param name
	 * @param parameterTypes
	 * @return
	 */
	public static MethodSignature createMethodSignature(String name, Class<?>... parameterTypes) {
		return new DefaultMethodSignature(name, parameterTypes);
	}

	/**
	 * 创建方法签名对象
	 *
	 * @param name
	 * @param parameterTypes
	 * @return
	 */
	public static MethodSignature createMethodSignature(String name, Collection<Class<?>> parameterTypes) {
		return new DefaultMethodSignature(name, parameterTypes);
	}

	/**
	 * 创建方法签名对象
	 *
	 * @param method
	 * @return
	 */
	public static MethodSignature createMethodSignature(Method method) {
		return new DefaultMethodSignature(method);
	}

}
