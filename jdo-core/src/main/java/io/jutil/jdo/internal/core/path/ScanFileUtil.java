package io.jutil.jdo.internal.core.path;

import java.net.URL;

/**
 * 扫描文件工具类
 *
 * @author Jin Zheng
 * @since 2022-03-02
 */
public class ScanFileUtil {
	private ScanFileUtil() {
	}

	/**
	 * file:/home/jar.jar!javax/servlet => /home/jar.jar
	 */
	public static String getRootPath(URL url) {
		String fileUrl = url.getFile();
		int pos = fileUrl.indexOf('!');
		if (pos == -1) {
			return fileUrl.substring(5);
		}

		return fileUrl.substring(5, pos);
	}

	/**
	 * com.blue.jdbc => com/blue/jdbc
	 */
	public static String dotToSplash(String name) {
		return name.replaceAll("\\.", "/");
	}

	/**
	 * JdbcObjectTemplate.class => JdbcObjectTemplate
	 */
	public static String trimExt(String name) {
		int pos = name.lastIndexOf('.');
		if (pos == -1) {
			return name;
		}

		return name.substring(0, pos);
	}

	/**
	 * javax/servlet/http/Cookie.class => javax.servlet.http.Cookie
	 */
	public static String fileToClass(String name) {
		name = name.replaceAll("/", ".");
		int pos = name.lastIndexOf('.');
		if (pos == -1) {
			return name;
		}

		return name.substring(0, pos);
	}

}
