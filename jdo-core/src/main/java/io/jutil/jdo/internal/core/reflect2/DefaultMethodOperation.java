package io.jutil.jdo.internal.core.reflect2;

import io.jutil.jdo.core.reflect2.MethodOperation;
import io.jutil.jdo.core.reflect2.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-01
 */
public abstract class DefaultMethodOperation extends DefaultExecutableOperation implements MethodOperation {
	private static Logger logger = LoggerFactory.getLogger(DefaultMethodOperation.class);

	private final static String SET_PREFIX = "set";
	private final static String GET_PREFIX = "get";
	private final static String IS_PREFIX = "is";

	protected final Method method;
	private final MethodSignature methodSignature;

	private boolean setter = false;
	private boolean getter = false;
	private String representField;

	protected DefaultMethodOperation(Method method, List<Class<?>> superClassList, List<Class<?>> interfaceList) {
		super(method, superClassList, interfaceList);
		this.method = method;
		this.methodSignature = new DefaultMethodSignature(method.getName(), method.getParameterTypes());
		this.parseRepresentField();
		if (logger.isDebugEnabled()) {
			logger.debug("方法名称: {}, 是否Setter: {}, 是否Getter: {}, 字段: {}, 注解: {}",
					this.getName(), setter, getter, representField, this.getAnnotations());
		}
	}

	@Override
	public MethodSignature getMethodSignature() {
		return methodSignature;
	}

	@Override
	public Class<?> getReturnType() {
		return method.getReturnType();
	}

	@Override
	public String getRepresentFieldName() {
		return representField;
	}

	@Override
	public boolean isSetter() {
		return setter;
	}

	@Override
	public boolean isGetter() {
		return getter;
	}

	@Override
	protected Executable findExecutable(Executable src, Class<?> clazz) {
		for (var m : clazz.getMethods()) {
			if (src.getName().equals(m.getName())
					&& Arrays.equals(src.getParameterTypes(), m.getParameterTypes())) {
				return m;
			}
		}
		return null;
	}

	private String fieldName() {
		String methodName = this.getName();
		String name = null;
		if (methodName.startsWith(IS_PREFIX)) {
			Class<?> returnType = method.getReturnType();
			if (returnType == Boolean.class || returnType == boolean.class) {
				name = methodName.substring(2);
			}
		}
		else if (methodName.startsWith(SET_PREFIX) || methodName.startsWith(GET_PREFIX)) {
			name = methodName.substring(3);
		}
		if (name == null || name.isEmpty()) {
			return null;
		}

		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	private void parseRepresentField() {
		String field = this.fieldName();
		if (field != null && !field.isEmpty()) {
			int paramSize = this.getParameterTypeList().size();
			if (paramSize == 0) {
				this.getter = true;
				this.representField = field;
			}
			else if (paramSize == 1) {
				this.setter = true;
				this.representField = field;
			}
		}
	}
}
