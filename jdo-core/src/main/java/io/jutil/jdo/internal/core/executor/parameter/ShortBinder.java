package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class ShortBinder implements ParameterBinder<Short> {


	@Override
	public Class<Short> getType() {
		return Short.class;
	}

	@Override
	public void bind(BindContext<Short> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setShort(i, value);
	}

	@Override
	public Short fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		var n = rs.getShort(i);
		return rs.wasNull() ? null : n;
	}

}
