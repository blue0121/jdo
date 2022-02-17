package io.jutil.jdo.internal.core.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

/**
 * 字符串转化为本地时间
 *
 * @author zhengj
 * @since 2022-02-17
 */
public class StringToLocalTimeConverter implements Converter<String, LocalTime>, DateTimeConverter {
	public StringToLocalTimeConverter() {
	}

	@Override
	public LocalTime convert(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		if (text.length() > TIME_LENGTH) {
			text = text.substring(text.length() - TIME_LENGTH);
		}

		return LocalTime.parse(text);
	}
}
