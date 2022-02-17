package io.jutil.jdo.internal.core.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

/**
 * 字符串转化为本地日期
 *
 * @author zhengj
 * @since 2022-02-17
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate>, DateTimeConverter {
	public StringToLocalDateConverter() {
	}

	@Override
	public LocalDate convert(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		if (text.length() > DATE_LENGTH) {
			text = text.substring(0, DATE_LENGTH);
		}

		return LocalDate.parse(text);
	}
}
