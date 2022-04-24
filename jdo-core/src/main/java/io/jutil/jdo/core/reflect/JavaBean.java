package io.jutil.jdo.core.reflect;

import io.jutil.jdo.internal.core.reflect.DefaultJavaBean;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * JavaBean
 *
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface JavaBean extends AnnotationOperation, ColumnNameOperation {
	/**
	 * 创建JavaBean反射对象
	 *
	 * @param target
	 * @param targetClass
	 * @return
	 */
	static JavaBean create(Object target, Class<?> targetClass) {
		return new DefaultJavaBean(target, targetClass);
	}

	/**
	 * 创建JavaBean反射对象
	 *
	 * @param targetClass
	 * @return
	 */
	static JavaBean create(Class<?> targetClass) {
		return new DefaultJavaBean(targetClass);
	}

	/**
	 * 目标对象类型
	 *
	 * @return
	 */
	Class<?> getTargetClass();

	/**
	 * 目标对象
	 *
	 * @return
	 */
	Object getTarget();

	/**
	 * 获取所有构造方法
	 *
	 * @return
	 */
	List<BeanConstructor> getAllConstructors();

	/**
	 * 根据参数类型获取构造方法
	 *
	 * @param types
	 * @return
	 */
	BeanConstructor getConstructor(Class<?>... types);

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
	List<BeanMethod> getAllMethods();

	/**
	 * 根据方法签名获取方法
	 *
	 * @param methodSignature
	 * @return
	 */
	BeanMethod getMethod(MethodSignature methodSignature);

	/**
	 * 获取所有字段
	 *
	 * @return
	 */
	Map<String, BeanField> getAllFields();

	/**
	 * 根据字段名获取字段
	 *
	 * @param fieldName
	 * @return
	 */
	BeanField getField(String fieldName);

	/**
	 * 根据对象获取所有字段值
	 *
	 * @param target object
	 * @return fieldName => fieldValue
	 */
	Map<String, Object> getAllFieldValues(Object target);

}
