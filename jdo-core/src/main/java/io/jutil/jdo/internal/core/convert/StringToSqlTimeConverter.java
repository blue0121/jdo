package io.jutil.jdo.internal.core.convert;


import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.sql.Time;

/**
 * 字符串转化为SQL时间
 *
 * @author zhengj
 * @since 2022-02-17
 */
@NoArgsConstructor
public class StringToSqlTimeConverter implements Converter<String, Time>, DateTimeConverter {


	@Override
	public Time convert(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		if (text.length() > TIME_LENGTH) {
			text = text.substring(text.length() - TIME_LENGTH);
		}

		return Time.valueOf(text);
	}
}
