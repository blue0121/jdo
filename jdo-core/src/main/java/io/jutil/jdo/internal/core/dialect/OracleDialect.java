package io.jutil.jdo.internal.core.dialect;

import io.jutil.jdo.core.annotation.LockModeType;
import lombok.NoArgsConstructor;

/**
 * Oracle 数据库方言
 *
 * @author Jin Zheng
 * @since 2022-02-18
 */
@NoArgsConstructor
public class OracleDialect implements Dialect {
	public static final String PROTOCOL = "oracle";
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
		StringBuilder t = new StringBuilder(sql.length() + 100);
		t.append("SELECT * FROM (SELECT TMP.*, ROWNUM RN FROM (");
		t.append(sql);
		t.append(") TMP WHERE ROWNUM <= ");
		t.append(start + size);
		t.append(") WHERE RN >= ");
		t.append(start + 1);
		return t.toString();
	}

	@Override
	public String lock(String sql, LockModeType type) {
		if (type == LockModeType.NONE) {
			return sql;
		}

		StringBuilder t = new StringBuilder(sql);
		if (type == LockModeType.WRITE) {
			t.append(" FOR UPDATE");
		}

		return t.toString();
	}
}
