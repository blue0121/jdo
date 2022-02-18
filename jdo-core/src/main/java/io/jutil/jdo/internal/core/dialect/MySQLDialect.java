package io.jutil.jdo.internal.core.dialect;

import io.jutil.jdo.core.annotation.LockModeType;

/**
 * MySQL 数据库方言
 *
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class MySQLDialect implements Dialect {
	public static final String PROTOCOL = "mysql";
	private static final String QUOT = "`";

	public MySQLDialect() {
	}

	@Override
	public String escape() {
		return QUOT;
	}

	@Override
	public String escape(String key) {
		StringBuilder t = new StringBuilder(key.length() + 4);
		t.append(QUOT).append(key).append(QUOT);
		return t.toString();
	}

	@Override
	public String page(String sql, int start, int size) {
		StringBuilder t = new StringBuilder(sql.length() + 16);
		t.append(sql);
		t.append(" limit ");
		t.append(start);
		t.append(",");
		t.append(size);
		return t.toString();
	}

	@Override
	public String lock(String sql, LockModeType type) {
		StringBuilder t = new StringBuilder(sql);
		switch (type) {
			case NONE:
				return sql;
			case READ:
				t.append(" lock in share mode");
				break;
			case WRITE:
				t.append(" for update");
				break;
			default:
				break;
		}

		return t.toString();
	}
}
