package io.jutil.jdo.internal.core.convert;

import io.jutil.jdo.internal.core.util.DateUtil;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
@NoArgsConstructor
public class LocalDateToDateConverter implements Converter<TemporalAccessor, Date> {

	@Override
	public Date convert(TemporalAccessor source) {
		if (source instanceof LocalDate) {
			return DateUtil.fromLocalDate((LocalDate) source);
		}

		if (source instanceof LocalTime) {
			return DateUtil.fromLocalTime((LocalTime) source);
		}

		if (source instanceof LocalDateTime) {
			return DateUtil.fromLocalDateTime((LocalDateTime) source);
		}

		throw new IllegalArgumentException("Unsupported type: " + source.getClass().getName());
	}
}
