package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class IntegerBinder implements ParameterBinder<Integer> {


	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	public void bind(BindContext<Integer> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setInt(i, value);
	}

	@Override
	public Integer fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		var n = rs.getInt(i);
		return rs.wasNull() ? null : n;
	}

}
