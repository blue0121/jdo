package io.jutil.jdo.internal.core.reflect2;

import io.jutil.jdo.core.collection.MultiMap;
import io.jutil.jdo.core.reflect2.ClassConstructorOperation;
import io.jutil.jdo.core.reflect2.ClassFieldOperation;
import io.jutil.jdo.core.reflect2.ClassMethodOperation;
import io.jutil.jdo.core.reflect2.ClassOperation;
import io.jutil.jdo.core.reflect2.MethodSignature;
import io.jutil.jdo.core.reflect2.NameOperation;
import io.jutil.jdo.internal.core.reflect.DefaultAnnotationOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-04-29
 */
public class DefaultClassOperation extends DefaultAnnotationOperation implements ClassOperation, NameOperation {
	private static Logger logger = LoggerFactory.getLogger(DefaultClassOperation.class);
	private static final Set<String> METHOD_SET = Set.of("wait", "equals", "toString", "hashCode", "getClass",
			"notify", "notifyAll");

	private final Class<?> targetClass;

	private List<Class<?>> superClassList;
	private List<Class<?>> interfaceList;

	private Map<MethodSignature, ClassConstructorOperation> constructorMap;

	private Map<String, ClassMethodOperation> getterMap;
	private MultiMap<String, ClassMethodOperation> setterMap;
	private List<ClassMethodOperation> allMethodList;
	private Map<MethodSignature, ClassMethodOperation> methodMap;

	private Map<String, ClassFieldOperation> fieldMap;

	public DefaultClassOperation(Class<?> targetClass) {
		super(targetClass);
		this.targetClass = targetClass;
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
		return null;
	}

	@Override
	public Object newInstance(Object... args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return null;
	}

	@Override
	public Object newInstanceQuietly(Object... args) {
		return null;
	}

	@Override
	public List<ClassMethodOperation> getAllMethods() {
		return null;
	}

	@Override
	public ClassMethodOperation getMethod(MethodSignature methodSignature) {
		return null;
	}

	@Override
	public Map<String, ClassFieldOperation> getAllFields() {
		return null;
	}

	@Override
	public ClassFieldOperation getField(String fieldName) {
		return null;
	}

	@Override
	public Map<String, Object> getAllFieldValues(Object target) {
		return null;
	}
}
