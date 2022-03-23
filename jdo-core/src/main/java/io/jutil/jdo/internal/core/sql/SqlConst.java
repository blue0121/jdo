package io.jutil.jdo.internal.core.sql;

/**
 * @author Jin Zheng
 * @since 2022-03-21
 */
public class SqlConst {
	public static final String PLACEHOLDER = "?";
	public static final String SEPARATOR = ",";
	public static final String EQUAL = "=";
	public static final String EQUAL_PLACEHOLDER = "=?";
	public static final String NOT_EQUAL_PLACEHOLDER = "!=?";
	public static final String AND = " and ";
	public static final String IN_PLACEHOLDER = " in (%s)";

	public static final String INSERT_TPL = "insert into %s (%s) values (%s)";
	public static final String UPDATE_TPL = "update %s set %s where %s";
	public static final String DELETE_TPL = "delete from %s where %s";
	public static final String SELECT_TPL = "select * from %s where %s";
	public static final String GET_TPL = "select %s from %s where %s";
	public static final String COUNT_TPL = "select count(*) from %s where %s";

	private SqlConst() {
	}

}
