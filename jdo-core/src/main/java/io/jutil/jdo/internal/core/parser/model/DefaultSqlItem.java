package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.util.AssertUtil;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@NoArgsConstructor
public class DefaultSqlItem implements SqlItem {
	private String sql;
	private List<String> paramNameList;


	public DefaultSqlItem(String sql) {
		this(sql, null);
	}

	public DefaultSqlItem(String sql, List<String> paramNameList) {
		this.sql = sql;
		this.setParamNameList(paramNameList);
	}

	@Override
	public String toString() {
		return String.format("SQL: %s, params: %s", sql, paramNameList);
	}

	@Override
	public void check() {
		AssertUtil.notEmpty(sql, "SQL");
		AssertUtil.notNull(paramNameList, "参数名称列表");
	}

	@Override
	public String getSql() {
		return sql;
	}

	@Override
	public List<String> getParamNameList() {
		return paramNameList;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setParamNameList(List<String> paramNameList) {
		if (paramNameList == null || paramNameList.isEmpty()) {
			this.paramNameList = List.of();
		} else {
			this.paramNameList = List.copyOf(paramNameList);
		}
	}
}
