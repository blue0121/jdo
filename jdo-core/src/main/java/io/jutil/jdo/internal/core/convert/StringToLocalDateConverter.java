package io.jutil.jdo.internal.core.convert;

import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

/**
 * 字符串转化为本地日期
 *
 * @author zhengj
 * @since 2022-02-17
 */
@NoArgsConstructor
public class StringToLocalDateConverter implements Converter<String, LocalDate>, DateTimeConverter {

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
