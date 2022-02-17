package io.jutil.jdo.internal.core.reflect;

import io.jutil.jdo.core.reflect.AnnotationOperation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public abstract class DefaultAnnotationOperation implements AnnotationOperation {
	private final AnnotatedElement annotatedElement;
	private Map<Class<?>, Annotation> annotationMap;
	private List<Annotation> annotationList;

	public DefaultAnnotationOperation(AnnotatedElement annotatedElement) {
		this.annotatedElement = annotatedElement;
	}

	@Override
	public final <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
		if (annotatedElement == null) {
			return null;
		}

		return annotatedElement.getDeclaredAnnotation(annotationClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return (T) annotationMap.get(annotationClass);
	}

	@Override
	public final List<Annotation> getDeclaredAnnotations() {
		if (annotatedElement == null) {
			return List.of();
		}

		Annotation[] annotations = annotatedElement.getDeclaredAnnotations();
		return Arrays.asList(annotations);
	}

	@Override
	public final List<Annotation> getAnnotations() {
		return annotationList;
	}

	/**
	 * 初始化注解Map
	 *
	 * @param annotationMap
	 */
	protected final void initAnnotationMap(Map<Class<?>, Annotation> annotationMap) {
		this.annotationMap = Map.copyOf(annotationMap);
		this.annotationList = List.copyOf(annotationMap.values());
	}

}
