package io.jutil.jdo.internal.core.expression;

import io.jutil.jdo.core.engine.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class DefaultExpression implements Expression {
	private ExpressionOperator operator;
	private List<Object> sqlList = new ArrayList<>();

	public DefaultExpression(ExpressionOperator operator) {
		this.operator = operator;
	}

	@Override
	public Expression add(String sql) {
		sqlList.add(sql);
		return this;
	}

	@Override
	public Expression add(Expression expression) {
		sqlList.add(expression);
		return this;
	}

	@Override
	public String toString() {
		if (sqlList.isEmpty()) {
			return "";
		}

		StringBuilder sql = new StringBuilder();
		for (Object obj : sqlList) {
			if (obj instanceof String str) {
				this.appendString(sql, str);
			} else if (obj instanceof Expression exp) {
				this.appendExpression(sql, exp);
			}
		}
		return sql.toString();
	}

	private void appendString(StringBuilder sql, String obj) {
		if (sql.length() > 0) {
			sql.append(operator);
		}
		sql.append(obj);
	}

	private void appendExpression(StringBuilder sql, Expression obj) {
		String str = obj.toString();
		if (str.isEmpty()) {
			return;
		}

		if (sql.length() > 0) {
			sql.append(operator);
		}
		sql.append("(").append(obj).append(")");
	}

}
