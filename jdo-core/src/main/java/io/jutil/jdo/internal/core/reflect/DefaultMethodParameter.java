package io.jutil.jdo.internal.core.reflect;

import io.jutil.jdo.core.reflect.MethodParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class DefaultMethodParameter implements MethodParameter {
	private static Logger logger = LoggerFactory.getLogger(DefaultMethodParameter.class);

	private final Parameter parameter;
	private final List<Parameter> superParamList;

	private Map<Class<?>, Annotation> annotationMap;
	private List<Annotation> annotationList;

	public DefaultMethodParameter(List<Parameter> superParamList) {
		this.superParamList = superParamList;
		this.parameter = superParamList.get(0);
		this.parseAnnotation();
		if (logger.isDebugEnabled()) {
			logger.debug("参数名称: {}, 注解: {}", this.getName(), annotationList);
		}
	}

	private void parseAnnotation() {
		Map<Class<?>, Annotation> annoMap = new HashMap<>();
		List<Annotation> annoList = new ArrayList<>();
		for (var param : superParamList) {
			for (var annotation : param.getDeclaredAnnotations()) {
				if (annoMap.containsKey(annotation.annotationType())) {
					continue;
				}

				annoMap.put(annotation.annotationType(), annotation);
				annoList.add(annotation);
			}
		}

		this.annotationMap = Map.copyOf(annoMap);
		this.annotationList = List.copyOf(annoList);
	}

	@Override
	public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
		return parameter.getDeclaredAnnotation(annotationClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return (T) annotationMap.get(annotationClass);
	}

	@Override
	public List<Annotation> getDeclaredAnnotations() {
		Annotation[] annotations = parameter.getDeclaredAnnotations();
		return Arrays.asList(annotations);
	}

	@Override
	public List<Annotation> getAnnotations() {
		return annotationList;
	}

	@Override
	public String getName() {
		return parameter.getName();
	}

	@Override
	public Class<?> getType() {
		return parameter.getType();
	}
}
