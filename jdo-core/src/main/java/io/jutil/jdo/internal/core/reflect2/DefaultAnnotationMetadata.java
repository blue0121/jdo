package io.jutil.jdo.internal.core.reflect2;

import io.jutil.jdo.core.reflect2.AnnotationMetadata;
import io.jutil.jdo.internal.core.util.AssertUtil;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-29
 */
@Setter
public class DefaultAnnotationMetadata implements AnnotationMetadata {
	private final Annotation annotation;
	private final Map<String, Object> map = new HashMap<>();

	public DefaultAnnotationMetadata(Annotation annotation) {
		this.annotation = annotation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation() {
		return (T) annotation;
	}

	@Override
	public Object getValue(String name) {
		AssertUtil.notEmpty(name, "配置项");
		var value = map.get(name);
		if (value == null) {
			throw new NullPointerException(name + "不存在");
		}

		return value;
	}

	@Override
	public int getIntValue(String name) {
		var value = this.getValue(name);
		if (value instanceof Integer i) {
			return i;
		} else if (value instanceof Number n) {
			return n.intValue();
		}
		throw new ClassCastException(name + "不是数字类型");
	}

	@Override
	public String getStringValue(String name) {
		var value = this.getValue(name);
		if (value instanceof CharSequence c) {
			return c.toString();
		}
		throw new ClassCastException(name + "不是字符串类型");
	}
}
