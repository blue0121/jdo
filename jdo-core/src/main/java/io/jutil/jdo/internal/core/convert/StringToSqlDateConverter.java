package io.jutil.jdo.internal.core.convert;

import org.springframework.core.convert.converter.Converter;

import java.sql.Date;

/**
 * 字符串转化为SQL日期
 *
 * @author zhengj
 * @since 2022-02-17
 */
public class StringToSqlDateConverter implements Converter<String, Date>, DateTimeConverter {
	public StringToSqlDateConverter() {
	}

	@Override
	public Date convert(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		if (text.length() > DATE_LENGTH) {
			text = text.substring(0, DATE_LENGTH);
		}

		return Date.valueOf(text);
	}
}
