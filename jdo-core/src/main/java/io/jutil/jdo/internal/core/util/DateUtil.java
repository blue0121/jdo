package io.jutil.jdo.internal.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author zhengj
 * @since 2022-02-17
 */
public class DateUtil {
	public static final String DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE = "yyyy-MM-dd";
	public static final String TIME = "HH:mm:ss";

	public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT);
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE);
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME);

	private DateUtil() {
	}

	/**
	 * 把日期对象转化为字符串，默认格式：yyyy-MM-dd HH:mm:ss
	 *
	 * @param date 日期对象
	 * @return 格式化后的字符串
	 */
	public static String formatDateTime(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(DEFAULT);
		return format.format(date);
	}

	/**
	 * 把日期对象转化为字符串，默认格式：yyyy-MM-dd
	 *
	 * @param date 日期对象
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(DATE);
		return format.format(date);
	}

	/**
	 * 把日期对象转化为字符串，默认格式：HH:mm:ss
	 *
	 * @param date 日期对象
	 * @return 格式化后的字符串
	 */
	public static String formatTime(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(TIME);
		return format.format(date);
	}

	/**
	 * 把日期对象转化为字符串
	 *
	 * @param date    日期对象
	 * @param pattern 格式模式，参见SimpleDateFormat
	 * @return 格式化后的字符串
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return null;
		}

		AssertUtil.notEmpty(pattern, "格式模式");
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 把字符串解析为日期对象，默认格式：yyyy-MM-dd HH:mm:ss
	 *
	 * @param text 字符串
	 * @return 日期对象，解析失败返回null
	 */
	public static Date parseDateTime(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(DEFAULT);
		try {
			Date date = format.parse(text);
			return date;
		}
		catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 把字符串解析为日期对象，默认格式：yyyy-MM-dd
	 *
	 * @param text 字符串
	 * @return 日期对象，解析失败返回null
	 */
	public static Date parseDate(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(DATE);
		try {
			Date date = format.parse(text);
			return date;
		}
		catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 把字符串解析为日期对象，默认格式：HH:mm:ss
	 *
	 * @param text 字符串
	 * @return 日期对象，解析失败返回null
	 */
	public static Date parseTime(String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(TIME);
		try {
			Date date = format.parse(text);
			return date;
		}
		catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 把字符串解析为日期对象
	 *
	 * @param text    字符串
	 * @param pattern 格式模式，参见SimpleDateFormat
	 * @return 日期对象，解析失败返回null
	 */
	public static Date parse(String text, String pattern) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		AssertUtil.notEmpty(pattern, "格式模式");
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			Date date = format.parse(text);
			return date;
		}
		catch (ParseException e) {
			return null;
		}
	}

	/**
	 * java.util.Date => java.time.LocalDate
	 */
	public static LocalDate toLocalDate(Date date) {
		if (date == null) {
			return null;
		}

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * java.util.Date => java.time.LocalDateTime
	 */
	public static LocalDateTime toLocalDateTime(Date date) {
		if (date == null) {
			return null;
		}

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * java.util.Date => java.time.LocalTime
	 */
	public static LocalTime toLocalTime(Date date) {
		if (date == null) {
			return null;
		}

		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
	}

	/**
	 * java.time.LocalDate => java.util.Date
	 */
	public static Date fromLocalDate(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}

		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * java.time.LocalDateTime => java.util.Date
	 */
	public static Date fromLocalDateTime(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}

		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * java.time.LocalTime => java.util.Date
	 */
	public static Date fromLocalTime(LocalTime localTime) {
		if (localTime == null) {
			return null;
		}

		return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
	}

}
