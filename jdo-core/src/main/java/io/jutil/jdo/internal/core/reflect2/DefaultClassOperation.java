package io.jutil.jdo.internal.core.reflect2;

import io.jutil.jdo.core.collection.MultiMap;
import io.jutil.jdo.core.reflect2.ClassConstructorOperation;
import io.jutil.jdo.core.reflect2.ClassFieldOperation;
import io.jutil.jdo.core.reflect2.ClassMethodOperation;
import io.jutil.jdo.core.reflect2.ClassOperation;
import io.jutil.jdo.core.reflect2.MethodSignature;
import io.jutil.jdo.internal.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
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
 * @since 2022-04-29
 */
public class DefaultClassOperation extends DefaultAnnotationOperation implements ClassOperation {
	private static Logger logger = LoggerFactory.getLogger(DefaultClassOperation.class);

	private final Class<?> targetClass;

	private List<Class<?>> superClassList;
	private List<Class<?>> interfaceList;

	private Map<String, ClassMethodOperation> getterMap;
	private MultiMap<String, ClassMethodOperation> setterMap;
	private List<ClassMethodOperation> allMethodList;
	private Map<MethodSignature, ClassMethodOperation> methodMap;

	private Map<String, ClassFieldOperation> fieldMap;

	public DefaultClassOperation(Class<?> targetClass) {
		super(targetClass);
		this.targetClass = targetClass;
		this.parseClass();
		if (logger.isDebugEnabled()) {
			logger.debug("类: {}, 超类: {}, 接口: {}, 注解: {}",
					this.getName(), superClassList, interfaceList, this.getAnnotations());
		}
		this.parseMethod();
		this.parseField();
	}

	@Override
	public Class<?> getTargetClass() {
		return targetClass;
	}

	@Override
	public String getName() {
		return targetClass.getSimpleName();
	}

	@Override
	public ClassConstructorOperation getConstructor(Class<?>... types) {
		try {
			var cons = targetClass.getConstructor(types);
			return new DefaultClassConstructorOperation(this, cons, superClassList);
		} catch (NoSuchMethodException e) {
			logger.warn("找不到构造方法, 类: {}, 参数: {}",
					targetClass.getName(), Arrays.toString(types));
			return null;
		}
	}

	@Override
	public Object newInstance(Object... args) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		var classes = this.getParameterClasses(args);
		var constructor = this.getConstructor(classes);
		if (constructor == null) {
			return null;
		}

		return constructor.newInstance(args);
	}

	@Override
	public Object newInstanceQuietly(Object... args) {
		var classes = this.getParameterClasses(args);
		var constructor = this.getConstructor(classes);
		if (constructor == null) {
			return null;
		}

		return constructor.newInstanceQuietly(args);
	}

	@Override
	public List<ClassMethodOperation> getAllMethods() {
		return allMethodList;
	}

	@Override
	public ClassMethodOperation getMethod(MethodSignature methodSignature) {
		return methodMap.get(methodSignature);
	}

	@Override
	public Map<String, ClassFieldOperation> getAllFields() {
		return fieldMap;
	}

	@Override
	public ClassFieldOperation getField(String fieldName) {
		return fieldMap.get(fieldName);
	}

	@Override
	public Map<String, Object> getAllFieldValues(Object target) {
		AssertUtil.notNull(target, "目标对象");

		Map<String, Object> map = new LinkedHashMap<>();
		for (var entry : fieldMap.entrySet()) {
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue().getFieldValue(target);
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
		var annotations = clazz.getDeclaredAnnotations();
		for (var annotation : annotations) {
			if (annotationMap.containsKey(annotation.annotationType())) {
				continue;
			}

			annotationMap.put(annotation.annotationType(), annotation);
		}
	}

	private void parseMethod() {
		Map<String, ClassMethodOperation> getter = new LinkedHashMap<>();
		MultiMap<String, ClassMethodOperation> setter = MultiMap.createLinked();
		List<ClassMethodOperation> all = new ArrayList<>();
		Map<MethodSignature, ClassMethodOperation> map = new HashMap<>();
		List<ClassMethodOperation> other = new ArrayList<>();
		Method[] methods = targetClass.getMethods();
		for (var method : methods) {
			int mod = method.getModifiers();
			if (Modifier.isStatic(mod) || RefelctConst.IGNORE_METHOD_SET.contains(method.getName())) {
				continue;
			}

			ClassMethodOperation beanMethod = new DefaultClassMethodOperation(this, method, superClassList, interfaceList);
			all.add(beanMethod);
			map.put(beanMethod.getMethodSignature(), beanMethod);
			if (beanMethod.isGetter()) {
				getter.put(beanMethod.getRepresentFieldName(), beanMethod);
			} else if (beanMethod.isSetter()) {
				setter.put(beanMethod.getRepresentFieldName(), beanMethod);
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
		Map<String, ClassFieldOperation> beanFieldMap = new LinkedHashMap<>();
		Map<String, Field> map = new LinkedHashMap<>();
		for (var clazz : superClassList) {
			this.filterField(clazz.getDeclaredFields(), map);
		}

		for (var entry : map.entrySet()) {
			var getter = getterMap.get(entry.getKey());
			var setterSet = setterMap.get(entry.getKey());
			var setter = this.getSetterMethod(getter, setterSet);
			var field = new DefaultClassFieldOperation(this, entry.getKey(), entry.getValue(), getter, setter);
			beanFieldMap.put(entry.getKey(), field);
		}
		for (var entry : getterMap.entrySet()) {
			if (beanFieldMap.containsKey(entry.getKey())) {
				continue;
			}

			var setterSet = setterMap.get(entry.getKey());
			var setter = this.getSetterMethod(entry.getValue(), setterSet);
			var field = new DefaultClassFieldOperation(this, entry.getKey(), null, entry.getValue(), setter);
			beanFieldMap.put(entry.getKey(), field);
		}
		for (var entry : setterMap.entrySet()) {
			if (entry.getValue().size() != 1 || beanFieldMap.containsKey(entry.getKey())) {
				continue;
			}

			var setter = entry.getValue().iterator().next();
			var getter = getterMap.get(entry.getKey());
			var field = new DefaultClassFieldOperation(this, entry.getKey(), null, getter, setter);
			beanFieldMap.put(entry.getKey(), field);
		}

		this.fieldMap = Collections.unmodifiableMap(beanFieldMap);
	}

	private ClassMethodOperation getSetterMethod(ClassMethodOperation getter, Set<ClassMethodOperation> setterSet) {
		if (setterSet == null || setterSet.isEmpty()) {
			return null;
		}

		if (getter == null) {
			if (setterSet.size() != 1) {
				return null;
			}

			return setterSet.iterator().next();
		}
		var returnType = getter.getReturnType();
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

	private Class<?>[] getParameterClasses(Object... params) {
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
