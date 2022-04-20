package io.jutil.jdo.internal.core.dialect;

import io.jutil.jdo.core.annotation.LockModeType;
import lombok.NoArgsConstructor;

/**
 * PostgreSQL数据库方言
 *
 * @author Jin Zheng
 * @since 2022-02-18
 */
@NoArgsConstructor
public class PostgreSQLDialect implements Dialect {
	public static final String PROTOCOL = "postgresql";
	private static final String QUOT = "\"";


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
		StringBuilder t = new StringBuilder(sql.length() + 24);
		t.append(sql);
		t.append(" limit ");
		t.append(size);
		t.append(" offset ");
		t.append(start);
		return t.toString();
	}

	@Override
	public String lock(String sql, LockModeType type) {
		if (type == LockModeType.NONE) {
			return sql;
		}

		StringBuilder t = new StringBuilder(sql);
		if (type == LockModeType.WRITE) {
			t.append(" for update");
		}

		return t.toString();
	}
}
