package io.jutil.jdo.internal.core.reflect;

import io.jutil.jdo.core.reflect.ParameterOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-29
 */
public class DefaultParameterOperation extends DefaultAnnotationOperation implements ParameterOperation {
	private static Logger logger = LoggerFactory.getLogger(DefaultParameterOperation.class);

	private final Parameter parameter;
	private final List<Parameter> parameterList;

	public DefaultParameterOperation(List<Parameter> parameterList) {
		super(parameterList.get(0));
		this.parameter = parameterList.get(0);
		this.parameterList = parameterList;
		this.parseAnnotation();
		if (logger.isDebugEnabled()) {
			logger.debug("参数名称: {}, 注解: {}", this.getName(), this.getAnnotations());
		}
	}

	private void parseAnnotation() {
		Map<Class<?>, Annotation> annotationMap = new HashMap<>();
		for (var param : parameterList) {
			for (var annotation : param.getDeclaredAnnotations()) {
				if (annotationMap.containsKey(annotation.annotationType())) {
					continue;
				}

				annotationMap.put(annotation.annotationType(), annotation);
			}
		}

		this.initAnnotationMap(annotationMap);
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
