package io.jutil.jdo.internal.core.reflect2;

import io.jutil.jdo.core.reflect2.ClassFieldOperation;
import io.jutil.jdo.core.reflect2.ClassMethodOperation;
import io.jutil.jdo.core.reflect2.ClassOperation;
import io.jutil.jdo.internal.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author Jin Zheng
 * @since 2022-05-05
 */
public class DefaultClassFieldOperation extends DefaultFieldOperation implements ClassFieldOperation {
	private static Logger logger = LoggerFactory.getLogger(DefaultClassFieldOperation.class);

	private final ClassOperation classOperation;
	private final ClassMethodOperation getterMethod;
	private final ClassMethodOperation setterMethod;
	
	public DefaultClassFieldOperation(ClassOperation classOperation, String fieldName, Field field,
	                                  ClassMethodOperation getterMethod, ClassMethodOperation setterMethod) {
		super(fieldName, field, getterMethod, setterMethod);
		this.classOperation = classOperation;
		this.getterMethod = getterMethod;
		this.setterMethod = setterMethod;
	}

	@Override
	public ClassOperation getClassOperation() {
		return classOperation;
	}

	@Override
	public ClassMethodOperation getGetterMethod() {
		return getterMethod;
	}

	@Override
	public ClassMethodOperation getSetterMethod() {
		return setterMethod;
	}

	@Override
	public Object getFieldValue(Object target) {
		AssertUtil.notNull(target, "目标对象");

		if (getterMethod != null) {
			return getterMethod.invokeQuietly(target);
		}
		if (field == null) {
			return null;
		}
		try {
			if (!field.canAccess(target)) {
				field.setAccessible(true);
			}
			return field.get(target);
		}
		catch (Exception e) {
			logger.error("获取字段值错误,", e);
			return null;
		}
	}

	@Override
	public boolean setFieldValue(Object target, Object value) {
		AssertUtil.notNull(target, "目标对象");
		if (value == null) {
			return true;
		}

		if (setterMethod != null) {
			setterMethod.invokeQuietly(target, value);
			return true;
		}
		if (field == null) {
			return false;
		}

		try {
			if (!field.canAccess(target)) {
				field.setAccessible(true);
			}
			field.set(target, value);
			return true;
		}
		catch (Exception e) {
			logger.error("设置字段值错误,", e);
			return false;
		}
	}
}
