package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class SqlDateBinder implements ParameterBinder<Date> {


	@Override
	public Class<Date> getType() {
		return Date.class;
	}

	@Override
	public void bind(BindContext<Date> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setDate(i, value);
	}

	@Override
	public Date fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		return rs.getDate(i);
	}

}
