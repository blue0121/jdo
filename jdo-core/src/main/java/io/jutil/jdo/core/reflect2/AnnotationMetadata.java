package io.jutil.jdo.core.reflect2;

import java.lang.annotation.Annotation;

/**
 * 注解元数据
 *
 * @author Jin Zheng
 * @since 2022-04-27
 */
public interface AnnotationMetadata {
	/**
	 * 注解
	 *
	 * @return
	 */
	<T extends Annotation> T getAnnotation();

	/**
	 * 根据注解方法名获取值
	 *
	 * @param name
	 * @return
	 */
	Object getValue(String name);

	/**
	 * 根据注解方法名获取整型值
	 *
	 * @param name
	 * @return
	 */
	int getIntValue(String name);

	/**
	 * 根据注解方法名获取字符串值
	 *
	 * @param name
	 * @return
	 */
	String getStringValue(String name);

}
