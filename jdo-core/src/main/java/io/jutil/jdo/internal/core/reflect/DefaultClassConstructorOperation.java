package io.jutil.jdo.internal.core.reflect;

import io.jutil.jdo.core.reflect.ClassConstructorOperation;
import io.jutil.jdo.core.reflect.ClassOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-01
 */
public class DefaultClassConstructorOperation extends DefaultExecutableOperation implements ClassConstructorOperation {
    private static Logger logger = LoggerFactory.getLogger(DefaultClassConstructorOperation.class);

    private final ClassOperation classOperation;
    private final Constructor<?> constructor;

	public DefaultClassConstructorOperation(ClassOperation classOperation, Constructor<?> constructor,
                                            List<Class<?>> superClassList) {
        super(constructor, superClassList, null);
        this.classOperation = classOperation;
        this.constructor = constructor;
        if (logger.isDebugEnabled()) {
            logger.debug("构造方法, 参数类型: {}, 注解: {}", this.getParameterTypeList(), this.getAnnotations());
        }
	}

    @Override
    public ClassOperation getClassOperation() {
        return classOperation;
    }

    @Override
    public Object newInstance(Object... args) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        if (!constructor.canAccess(null)) {
            constructor.setAccessible(true);
        }
        return constructor.newInstance(args);
    }

    @Override
    public Object newInstanceQuietly(Object... args) {
        Object value = null;
        try {
            value = this.newInstance(args);
        }
        catch (Exception e) {
            logger.error("实例化对象错误,", e);
        }
        return value;
    }

    @Override
    protected Executable findExecutable(Executable src, Class<?> clazz) {
        if (clazz.isInterface()) {
            return null;
        }

        for (var cst : clazz.getConstructors()) {
            if (Arrays.equals(src.getParameterTypes(), cst.getParameterTypes())) {
                return cst;
            }
        }
        return null;
    }
}
