package io.jutil.jdo.internal.core.expression;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public enum ExpressionOperator {

	AND(" and "),

	OR(" or ");


	private String name;

	ExpressionOperator(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
