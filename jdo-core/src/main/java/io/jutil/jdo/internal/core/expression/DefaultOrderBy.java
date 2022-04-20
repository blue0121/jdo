package io.jutil.jdo.internal.core.expression;

import io.jutil.jdo.core.engine.OrderBy;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
@NoArgsConstructor
public class DefaultOrderBy implements OrderBy {
	private List<String> sqlList = new ArrayList<>();


	@Override
	public void add(String sql) {
		sqlList.add(sql);
	}

	@Override
	public String toString() {
		if (sqlList.isEmpty()) {
			return "";
		}

		StringBuilder sql = new StringBuilder();
		for (String obj : sqlList) {
			if (sql.length() > 0) {
				sql.append(",");
			}
			sql.append(obj);
		}
		return sql.toString();
	}
}
