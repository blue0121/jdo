package io.jutil.jdo.internal.core.reflect2;

import io.jutil.jdo.core.reflect2.FieldOperation;
import io.jutil.jdo.core.reflect2.MethodOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-05-01
 */
public class DefaultFIeldOperation extends DefaultAnnotationOperation implements FieldOperation {
	private static Logger logger = LoggerFactory.getLogger(DefaultFIeldOperation.class);

	private final Field field;
	private final MethodOperation getterMethod;
	private final MethodOperation setterMethod;

	private Map<Class<?>, Annotation> getterAnnotationMap;
	private List<Annotation> getterAnnotationList;
	private Map<Class<?>, Annotation> setterAnnotationMap;
	private List<Annotation> setterAnnotationList;

	public DefaultFIeldOperation(Field field, MethodOperation getterMethod, MethodOperation setterMethod) {
		super(field);
		this.field = field;
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;

	}

	@Override
	public Class<?> getType() {
		return field.getType();
	}

	@Override
	public int getModifiers() {
		return field.getModifiers();
	}

	@Override
	public <T extends Annotation> T getGetterAnnotation(Class<?> annotationClass) {
		return null;
	}

	@Override
	public List<Annotation> getGetterAnnotations() {
		return null;
	}

	@Override
	public <T extends Annotation> T getSetterAnnotation(Class<?> annotationClass) {
		return null;
	}

	@Override
	public List<Annotation> getSetterAnnotations() {
		return null;
	}

	@Override
	public String getName() {
		return field.getName();
	}
}
