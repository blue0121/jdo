package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class DateBinder implements ParameterBinder<Date> {


    @Override
    public Class<Date> getType() {
        return Date.class;
    }

	@Override
	public void bind(BindContext<Date> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = new Timestamp(context.getValue().getTime());
		pstmt.setTimestamp(i, value);
	}

	@Override
	public Date fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		var d = rs.getTimestamp(i);
		return rs.wasNull() ? null : new Date(d.getTime());
	}

    @Override
    public void bind(PreparedStatement pstmt, int i, Date val) throws SQLException {
		var times = val.getTime();
		pstmt.setTimestamp(i, new Timestamp(times));
    }

	@Override
	public Date fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var d = rs.getTimestamp(i);
		return rs.wasNull() ? null : new Date(d.getTime());
	}
}
