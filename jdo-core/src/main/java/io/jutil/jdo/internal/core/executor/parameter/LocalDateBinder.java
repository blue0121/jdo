package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class LocalDateBinder implements ParameterBinder<LocalDate> {


	@Override
	public Class<LocalDate> getType() {
		return LocalDate.class;
	}

	@Override
	public void bind(BindContext<LocalDate> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setDate(i, Date.valueOf(value));
	}

	@Override
	public LocalDate fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		var d = rs.getDate(i);
		return rs.wasNull() ? null : d.toLocalDate();
	}

}
