package io.jutil.jdo.internal.core.reflect;

import io.jutil.jdo.core.collection.MultiMap;
import io.jutil.jdo.core.reflect.BeanConstructor;
import io.jutil.jdo.core.reflect.BeanField;
import io.jutil.jdo.core.reflect.BeanMethod;
import io.jutil.jdo.core.reflect.JavaBean;
import io.jutil.jdo.core.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class DefaultJavaBean extends DefaultAnnotationOperation implements JavaBean {
	private static Logger logger = LoggerFactory.getLogger(DefaultJavaBean.class);
	private static final Set<String> METHOD_SET = Set.of("wait", "equals", "toString", "hashCode", "getClass",
			"notify", "notifyAll");

	private final Object target;
	private final Class<?> targetClass;

	private List<Class<?>> superClassList;
	private List<Class<?>> interfaceList;

	private Map<ParameterTypeKey, BeanConstructor> constructorMap;
	private List<BeanConstructor> constructorList;

	private Map<String, BeanMethod> getterMap;
	private MultiMap<String, BeanMethod> setterMap;
	private List<BeanMethod> allMethodList;
	private Map<MethodSignature, BeanMethod> methodMap;

	private Map<String, BeanField> fieldMap;

	public DefaultJavaBean(Class<?> targetClass) {
		this(null, targetClass);
	}

	public DefaultJavaBean(Object target, Class<?> targetClass) {
		super(targetClass);
		this.target = target;
		this.targetClass = targetClass;
		this.parseClass();
		if (logger.isDebugEnabled()) {
			logger.debug("类: {}, 超类: {}, 接口: {}, 注解: {}",
					this.getName(), superClassList, interfaceList, this.getAnnotations());
		}
		this.parseConstructor();
		this.parseMethod();
		this.parseField();
	}

	@Override
	public String getName() {
		return targetClass.getSimpleName();
	}

	@Override
	public Class<?> getTargetClass() {
		return targetClass;
	}

	@Override
	public Object getTarget() {
		return target;
	}

	@Override
	public List<BeanConstructor> getAllConstructors() {
		return constructorList;
	}

	@Override
	public BeanConstructor getConstructor(Class<?>... types) {
		ParameterTypeKey key = new ParameterTypeKey(types);
		BeanConstructor constructor = constructorMap.get(key);
		if (constructor != null) {
			return constructor;
		}

		try {
			Constructor<?> cons = targetClass.getConstructor(types);
			constructor = new DefaultBeanConstructor(cons, superClassList);
		} catch (NoSuchMethodException e) {
			logger.warn("找不到构造方法, 类: {}, 参数: {}",
					targetClass.getName(), Arrays.toString(types));
		}
		return constructor;
	}

	@Override
	public Object newInstance(Object... args) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<?>[] classes = this.getParamClasses(args);
		BeanConstructor constructor = this.getConstructor(classes);
		if (constructor == null) {
			return null;
		}

		return constructor.newInstance(args);
	}

	@Override
	public Object newInstanceQuietly(Object... args) {
		Class<?>[] classes = this.getParamClasses(args);
		BeanConstructor constructor = this.getConstructor(classes);
		if (constructor == null) {
			return null;
		}

		return constructor.newInstanceQuietly(args);
	}

	@Override
	public List<BeanMethod> getAllMethods() {
		return allMethodList;
	}

	@Override
	public BeanMethod getMethod(MethodSignature methodSignature) {
		return methodMap.get(methodSignature);
	}

	@Override
	public Map<String, BeanField> getAllFields() {
		return fieldMap;
	}

	@Override
	public BeanField getField(String fieldName) {
		return fieldMap.get(fieldName);
	}

	@Override
	public Map<String, Object> getAllFieldValues(Object target) {
		var instance = target == null ? this.target : target;
		if (instance == null) {
			logger.warn("目标对象为空");
			return Map.of();
		}
		Map<String, Object> map = new LinkedHashMap<>();
		for (var entry : fieldMap.entrySet()) {
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue().getFieldValue(instance);
			map.put(fieldName, fieldValue);
		}
		return map;
	}

	private void parseClass() {
		List<Class<?>> superList = new ArrayList<>();
		List<Class<?>> interList = new ArrayList<>();
		Map<Class<?>, Annotation> annotationMap = new HashMap<>();
		this.parseSuperClass(targetClass, superList, interList, annotationMap);
		for (var cls : superList) {
			for (var inter : cls.getInterfaces()) {
				this.parseSuperClass(inter, superList, interList, annotationMap);
			}
		}
		this.superClassList = List.copyOf(superList);
		this.interfaceList = List.copyOf(interList);
		this.initAnnotationMap(annotationMap);
	}

	private void parseSuperClass(Class<?> cls, List<Class<?>> superList, List<Class<?>> interList,
	                             Map<Class<?>, Annotation> annotationMap) {
		Queue<Class<?>> queue = new LinkedList<>();
		queue.offer(cls);
		Class<?> clazz = null;
		while ((clazz = queue.poll()) != null) {
			if (clazz == Object.class) {
				continue;
			}

			if (clazz.isInterface()) {
				interList.add(clazz);
			} else {
				superList.add(clazz);
			}
			this.parseAnnotation(clazz, annotationMap);
			queue.offer(clazz.getSuperclass());
		}
	}

	private void parseAnnotation(Class<?> clazz, Map<Class<?>, Annotation> annotationMap) {
		Annotation[] annotations = clazz.getDeclaredAnnotations();
		for (var annotation : annotations) {
			if (annotationMap.containsKey(annotation.annotationType())) {
				continue;
			}

			annotationMap.put(annotation.annotationType(), annotation);
		}
	}

	private void parseConstructor() {
		Map<ParameterTypeKey, BeanConstructor> consMap = new HashMap<>();
		Constructor<?>[] constructors = targetClass.getConstructors();
		for (var constructor : constructors) {
			BeanConstructor beanConstructor = new DefaultBeanConstructor(constructor, superClassList);
			ParameterTypeKey key = new ParameterTypeKey(constructor.getParameterTypes());
			consMap.put(key, beanConstructor);
		}
		this.constructorMap = Map.copyOf(consMap);
		this.constructorList = List.copyOf(consMap.values());
	}

	private void parseMethod() {
		Map<String, BeanMethod> getter = new LinkedHashMap<>();
		MultiMap<String, BeanMethod> setter = MultiMap.createLinked();
		List<BeanMethod> all = new ArrayList<>();
		Map<MethodSignature, BeanMethod> map = new HashMap<>();
		List<BeanMethod> other = new ArrayList<>();
		Method[] methods = targetClass.getMethods();
		for (var method : methods) {
			int mod = method.getModifiers();
			if (Modifier.isStatic(mod) || METHOD_SET.contains(method.getName())) {
				continue;
			}

			BeanMethod beanMethod = new DefaultBeanMethod(target, method, superClassList, interfaceList);
			all.add(beanMethod);
			map.put(beanMethod.getMethodSignature(), beanMethod);
			if (beanMethod.isGetter()) {
				getter.put(beanMethod.getRepresentField(), beanMethod);
			} else if (beanMethod.isSetter()) {
				setter.put(beanMethod.getRepresentField(), beanMethod);
			} else {
				other.add(beanMethod);
			}
		}
		this.getterMap = Collections.unmodifiableMap(getter);
		this.setterMap = MultiMap.copyOf(setter);
		this.allMethodList = List.copyOf(all);
		this.methodMap = Map.copyOf(map);
	}

	private void parseField() {
		Map<String, BeanField> beanFieldMap = new LinkedHashMap<>();
		Map<String, Field> map = new LinkedHashMap<>();
		this.filterField(this.targetClass.getDeclaredFields(), map);
		this.filterField(this.targetClass.getFields(), map);
		for (var entry : map.entrySet()) {
			BeanMethod getter = getterMap.get(entry.getKey());
			Set<BeanMethod> setterSet = setterMap.get(entry.getKey());
			BeanMethod setter = this.getSetterMethod(getter, setterSet);
			BeanField field = new DefaultBeanField(entry.getKey(), target, entry.getValue(), getter, setter);
			beanFieldMap.put(entry.getKey(), field);
		}
		for (var entry : getterMap.entrySet()) {
			if (beanFieldMap.containsKey(entry.getKey())) {
				continue;
			}

			Set<BeanMethod> setterSet = setterMap.get(entry.getKey());
			BeanMethod setter = this.getSetterMethod(entry.getValue(), setterSet);
			BeanField field = new DefaultBeanField(entry.getKey(), target, null, entry.getValue(), setter);
			beanFieldMap.put(entry.getKey(), field);
		}
		for (var entry : setterMap.entrySet()) {
			if (entry.getValue().size() != 1 || beanFieldMap.containsKey(entry.getKey())) {
				continue;
			}

			BeanMethod setter = entry.getValue().iterator().next();
			BeanMethod getter = getterMap.get(entry.getKey());
			BeanField field = new DefaultBeanField(entry.getKey(), target, null, getter, setter);
			beanFieldMap.put(entry.getKey(), field);
		}

		this.fieldMap = Collections.unmodifiableMap(beanFieldMap);
	}

	private BeanMethod getSetterMethod(BeanMethod getter, Set<BeanMethod> setterSet) {
		if (setterSet == null || setterSet.isEmpty()) {
			return null;
		}

		if (getter == null) {
			if (setterSet.size() != 1) {
				return null;
			}

			return setterSet.iterator().next();
		}
		Class<?> returnType = getter.getReturnType();
		for (var setter : setterSet) {
			if (setter.getParameterTypeList() == null || setter.getParameterTypeList().size() != 1) {
				continue;
			}

			if (returnType == setter.getParameterTypeList().get(0)) {
				return setter;
			}
		}
		return null;
	}

	private void filterField(Field[] fields, Map<String, Field> map) {
		for (var field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isFinal(mod) || Modifier.isStatic(mod)) {
				continue;
			}
			if (map.containsKey(field.getName())) {
				continue;
			}

			map.put(field.getName(), field);
		}
	}

	private Class<?>[] getParamClasses(Object... params) {
		if (params.length == 0) {
			return new Class<?>[0];
		}

		Class<?>[] classes = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			classes[i] = params[i].getClass();
		}
		return classes;
	}

}
