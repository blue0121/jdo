package io.jutil.jdo.internal.core.convert;

import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;

/**
 * 字符串转化为SQL时间戳
 *
 * @author zhengj
 * @since 2022-02-17
 */
@NoArgsConstructor
public class StringToSqlTimestampConverter implements Converter<String, Timestamp>, DateTimeConverter {


	@Override
	public Timestamp convert(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		if (!text.contains(BLANK)) {
			text += " 00:00:00";
		}

		return Timestamp.valueOf(text);
	}
}
