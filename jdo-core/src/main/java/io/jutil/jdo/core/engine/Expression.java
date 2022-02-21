package io.jutil.jdo.core.engine;

import io.jutil.jdo.internal.core.expression.DefaultExpression;
import io.jutil.jdo.internal.core.expression.DefaultOrderBy;
import io.jutil.jdo.internal.core.expression.ExpressionOperator;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public interface Expression {

	Expression add(String sql);

	Expression add(Expression expression);

	static Expression and() {
		return new DefaultExpression(ExpressionOperator.AND);
	}

	static Expression or() {
		return new DefaultExpression(ExpressionOperator.OR);
	}

	static OrderBy orderBy() {
		return new DefaultOrderBy();
	}

}
