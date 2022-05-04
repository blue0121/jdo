package io.jutil.jdo.core.reflect2;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 注解操作
 *
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface AnnotationOperation {

	/**
	 * 根据注解类型获取注解，包括超类和接口
	 *
	 * @param annotationClass
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T getAnnotation(Class<T> annotationClass);

	/**
	 * 获取所有注解，包括超类和接口
	 *
	 * @return
	 */
	List<Annotation> getAnnotations();

}
