package io.jutil.jdo.internal.core.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class DateToLocalDateConverterFactory implements ConverterFactory<Date, TemporalAccessor> {
	public DateToLocalDateConverterFactory() {
	}

	@Override
	public <T extends TemporalAccessor> Converter<Date, T> getConverter(Class<T> targetType) {
		if (targetType == LocalDate.class || targetType == LocalTime.class
				|| targetType == LocalDateTime.class) {
			return new DateToLocalDate<>(targetType);
		}

		return null;
	}

	private class DateToLocalDate<T extends TemporalAccessor> implements Converter<Date, T> {
		private final Class<T> clazz;

		public DateToLocalDate(Class<T> clazz) {
			this.clazz = clazz;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T convert(Date source) {
			if (clazz == LocalDate.class) {
				return (T) source.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}

			if (clazz == LocalTime.class) {
				return (T) source.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
			}

			if (clazz == LocalDateTime.class) {
				return (T) source.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			}

			return null;
		}
	}
}
