package io.jutil.jdo.internal.core.reflect;

import io.jutil.jdo.core.reflect.BeanField;
import io.jutil.jdo.core.reflect.BeanMethod;
import io.jutil.jdo.internal.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class DefaultBeanField extends DefaultAnnotationOperation implements BeanField {
	private static Logger logger = LoggerFactory.getLogger(DefaultBeanField.class);

	private final String fieldName;
	private final Object target;
	private final Field field;
	private final BeanMethod getterMethod;
	private final BeanMethod setterMethod;

	private Map<Class<?>, Annotation> getterAnnotationMap;
	private List<Annotation> getterAnnotationList;
	private Map<Class<?>, Annotation> setterAnnotationMap;
	private List<Annotation> setterAnnotationList;

	public DefaultBeanField(String fieldName, Object target, Field field,
	                        BeanMethod getterMethod, BeanMethod setterMethod) {
		super(field);
		this.fieldName = fieldName;
		this.target = target;
		this.field = field;
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
		this.parseAnnotation();
		if (logger.isDebugEnabled()) {
			logger.debug("字段: {}, {}, Setter方法: {}, Getter方法: {}, 字段注解: {}," +
							" Getter方法注解: {}, Getter方法注解: {}",
					fieldName, field != null, setterMethod != null ? setterMethod.getName() : null,
					getterMethod != null ? getterMethod.getName() : null,
					this.getAnnotations(), getterAnnotationList, setterAnnotationList);
		}
	}

	private void parseAnnotation() {
		Map<Class<?>, Annotation> annotationMap = new HashMap<>();
		if (field != null) {
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				annotationMap.put(annotation.annotationType(), annotation);
			}
		}
		this.initAnnotationMap(annotationMap);

		var getterMap = this.parseAnnotationWithMethod(annotationMap, getterMethod);
		this.getterAnnotationMap = Map.copyOf(getterMap);
		this.getterAnnotationList = List.copyOf(getterMap.values());

		var setterMap = this.parseAnnotationWithMethod(annotationMap, setterMethod);
		this.setterAnnotationMap = Map.copyOf(setterMap);
		this.setterAnnotationList = List.copyOf(setterMap.values());
	}

	private Map<Class<?>, Annotation> parseAnnotationWithMethod(Map<Class<?>, Annotation> annotationMap,
	                                                            BeanMethod method) {
		Map<Class<?>, Annotation> methodAnnotationMap = new HashMap<>();
		if (method != null) {
			method.getAnnotations().forEach(e -> methodAnnotationMap.put(e.annotationType(), e));
		}
		for (var entry : annotationMap.entrySet()) {
			if (methodAnnotationMap.containsKey(entry.getKey())) {
				continue;
			}

			methodAnnotationMap.put(entry.getKey(), entry.getValue());
		}
		return methodAnnotationMap;
	}

	@Override
	public String getName() {
		return fieldName;
	}

	@Override
	public Object getTarget() {
		return target;
	}

	@Override
	public Field getField() {
		return field;
	}

	@Override
	public Object getFieldValue(Object target) {
		Object tar = target == null ? this.target : target;
		AssertUtil.notNull(tar, "目标对象");

		Object value = getterMethod != null ? getterMethod.invokeQuietly(tar) : null;
		if (value == null && field != null) {
			try {
				if (!field.canAccess(tar)) {
					field.setAccessible(true);
				}
				value = field.get(tar);
			}
			catch (Exception e) {
				logger.error("获取字段值错误,", e);
			}
		}
		return value;
	}

	@Override
	public boolean setFieldValue(Object target, Object value) {
		Object tar = target == null ? this.target : target;
		AssertUtil.notNull(tar, "目标对象");

		boolean flag = false;
		if (setterMethod != null) {
			setterMethod.invokeQuietly(tar, value);
			flag = true;
		}
		else if (field != null) {
			try {
				if (!field.canAccess(tar)) {
					field.setAccessible(true);
				}
				field.set(tar, value);
				flag = true;
			}
			catch (Exception e) {
				logger.error("设置字段值错误,", e);
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public BeanMethod getGetterMethod() {
		return getterMethod;
	}

	@Override
	public BeanMethod getSetterMethod() {
		return setterMethod;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getGetterAnnotation(Class<T> annotationClass) {
		return (T) getterAnnotationMap.get(annotationClass);
	}

	@Override
	public List<Annotation> getGetterAnnotations() {
		return getterAnnotationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getSetterAnnotation(Class<T> annotationClass) {
		return (T) setterAnnotationMap.get(annotationClass);
	}

	@Override
	public List<Annotation> getSetterAnnotations() {
		return setterAnnotationList;
	}

}
