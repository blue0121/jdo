package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class LocalTimeBinder implements ParameterBinder<LocalTime> {


    @Override
    public Class<LocalTime> getType() {
        return LocalTime.class;
    }

	@Override
	public void bind(BindContext<LocalTime> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setTime(i, Time.valueOf(value));
	}

	@Override
	public LocalTime fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		var d = rs.getTime(i);
		return rs.wasNull() ? null : d.toLocalTime();
	}

}
