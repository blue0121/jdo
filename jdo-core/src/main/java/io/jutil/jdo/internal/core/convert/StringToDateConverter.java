package io.jutil.jdo.internal.core.convert;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Spring3，字符串转化为日期时间
 *
 * @author zhengj
 * @since 2022-02-17
 */
@NoArgsConstructor
public class StringToDateConverter implements Converter<String, Date>, DateTimeConverter {
	private static Logger logger = LoggerFactory.getLogger(StringToDateConverter.class);


	@Override
	public Date convert(String text) {
		Date val = null;
		if (text == null || text.isEmpty()) {
			return val;
		}

		try {
			if (text.contains(BLANK)) {
				SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
				val = dateTimeFormat.parse(text);
			} else {
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				val = dateFormat.parse(text);
			}
		} catch (ParseException e) {
			logger.error(text + " 不是有效的日期时间格式", e);
		}
		return val;
	}
}
