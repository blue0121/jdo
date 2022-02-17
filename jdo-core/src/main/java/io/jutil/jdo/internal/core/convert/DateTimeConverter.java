package io.jutil.jdo.internal.core.convert;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface DateTimeConverter {
	String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	String DATE_FORMAT = "yyyy-MM-dd";
	String TIME_FORMAT = "HH:mm:ss";

	String BLANK = " ";

	int DATE_LENGTH = 10;
	int TIME_LENGTH = 8;
}
