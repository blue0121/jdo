package io.jutil.jdo.core.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 字段
 *
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface BeanField extends AnnotationOperation, ColumnNameOperation {

	/**
	 * 目标对象
	 *
	 * @return
	 */
	Object getTarget();

	/**
	 * 字段对象
	 *
	 * @return
	 */
	Field getField();

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
	 * @param value
	 * @return
	 */
	boolean setFieldValue(Object target, Object value);

	/**
	 * 关联Getter方法
	 *
	 * @return
	 */
	BeanMethod getGetterMethod();

	/**
	 * 关联Setter方法
	 *
	 * @return
	 */
	BeanMethod getSetterMethod();

	/**
	 * 根据注解类型获取Getter方法注解，包括超类和接口
	 *
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T getGetterAnnotation(Class<T> annotationClass);

	/**
	 * 获取Getter方法所有注解，包括超类和接口
	 *
	 * @return
	 */
	List<Annotation> getGetterAnnotations();

	/**
	 * 根据注解类型获取Setter方法注解，包括超类和接口
	 *
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T getSetterAnnotation(Class<T> annotationClass);

	/**
	 * 获取Setter方法所有注解，包括超类和接口
	 *
	 * @return
	 */
	List<Annotation> getSetterAnnotations();

}
