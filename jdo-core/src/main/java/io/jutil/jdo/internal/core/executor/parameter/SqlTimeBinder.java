package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.sql.Time;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class SqlTimeBinder implements ParameterBinder<Time> {


	@Override
	public Class<Time> getType() {
		return Time.class;
	}

	@Override
	public void bind(BindContext<Time> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setTime(i, value);
	}

	@Override
	public Time fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		return rs.getTime(i);
	}

}
