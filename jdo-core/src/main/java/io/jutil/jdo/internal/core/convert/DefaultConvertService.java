package io.jutil.jdo.internal.core.convert;

import io.jutil.jdo.core.convert.ConvertService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class DefaultConvertService implements ConvertService {
	private DefaultConversionService conversionService;

	public DefaultConvertService() {
		conversionService = (DefaultConversionService) DefaultConversionService.getSharedInstance();
		conversionService.addConverter(new StringToDateConverter());
		conversionService.addConverter(new StringToLocalDateConverter());
		conversionService.addConverter(new StringToLocalDateTimeConverter());
		conversionService.addConverter(new StringToLocalTimeConverter());
		conversionService.addConverter(new StringToSqlDateConverter());
		conversionService.addConverter(new StringToSqlTimeConverter());
		conversionService.addConverter(new StringToSqlTimestampConverter());
		conversionService.addConverter(LocalDate.class, Date.class, new LocalDateToDateConverter());
		conversionService.addConverter(LocalTime.class, Date.class, new LocalDateToDateConverter());
		conversionService.addConverter(LocalDateTime.class, Date.class, new LocalDateToDateConverter());

		conversionService.addConverterFactory(new DateToLocalDateConverterFactory());
	}

	@Override
	public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
		return conversionService.canConvert(sourceType, targetType);
	}

	@Override
	public <T> T convert(Object source, Class<T> targetType) {
		return conversionService.convert(source, targetType);
	}

	public void addConverter(GenericConverter converter) {
		conversionService.addConverter(converter);
	}

	public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
		conversionService.addConverter(sourceType, targetType, converter);
	}

	public void addConverterFactory(ConverterFactory<?, ?> factory) {
		conversionService.addConverterFactory(factory);
	}

}
