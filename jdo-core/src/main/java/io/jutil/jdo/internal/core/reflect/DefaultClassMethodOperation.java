package io.jutil.jdo.internal.core.reflect;

import io.jutil.jdo.core.reflect.ClassMethodOperation;
import io.jutil.jdo.core.reflect.ClassOperation;
import io.jutil.jdo.internal.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-01
 */
public class DefaultClassMethodOperation extends DefaultMethodOperation implements ClassMethodOperation {
	private static Logger logger = LoggerFactory.getLogger(DefaultClassMethodOperation.class);

	private final ClassOperation classOperation;

	public DefaultClassMethodOperation(ClassOperation classOperation, Method method,
	                                   List<Class<?>> superClassList, List<Class<?>> interfaceList) {
		super(method, superClassList, interfaceList);
		this.classOperation = classOperation;
	}

	@Override
	public ClassOperation getClassOperation() {
		return classOperation;
	}

	@Override
	public Object invoke(Object target, Object... args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		AssertUtil.notNull(target, "目标对象");
		return method.invoke(target, args);
	}

	@Override
	public Object invokeQuietly(Object target, Object... args) {
		Object value = null;
		try {
			value = this.invoke(target, args);
		}
		catch (Exception e) {
			logger.error("调用方法错误,", e);
		}
		return value;
	}
}
