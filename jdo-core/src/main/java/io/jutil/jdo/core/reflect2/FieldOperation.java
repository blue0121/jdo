package io.jutil.jdo.core.reflect2;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-04-28
 */
public interface FieldOperation extends AnnotationOperation, NameOperation {

	/**
	 * 字段名称
	 *
	 * @return
	 */
	String getFieldName();

	/**
	 * 字段类型
	 *
	 * @return
	 */
	Class<?> getType();

	/**
	 * 字段的可见性
	 *
	 * @return
	 */
	int getModifiers();

	/**
	 * 根据注解类型获取Getter方法注解，包括超类和接口
	 *
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T getGetterAnnotation(Class<?> annotationClass);

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
	<T extends Annotation> T getSetterAnnotation(Class<?> annotationClass);

	/**
	 * 获取Setter方法所有注解，包括超类和接口
	 *
	 * @return
	 */
	List<Annotation> getSetterAnnotations();

}
