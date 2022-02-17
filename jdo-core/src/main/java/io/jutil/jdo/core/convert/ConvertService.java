package io.jutil.jdo.core.convert;


/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface ConvertService {

	boolean canConvert(Class<?> sourceType, Class<?> targetType);

	<T> T convert(Object source, Class<T> targetType);

}
