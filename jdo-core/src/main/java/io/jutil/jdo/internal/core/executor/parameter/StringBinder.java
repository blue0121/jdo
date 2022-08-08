package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class StringBinder implements ParameterBinder<String> {


	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public void bind(BindContext<String> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setString(i, value);
	}

	@Override
	public String fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		return rs.getString(i);
	}

}
