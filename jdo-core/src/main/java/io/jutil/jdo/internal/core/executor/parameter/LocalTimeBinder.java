package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class LocalTimeBinder implements ParameterBinder<LocalTime> {
	public LocalTimeBinder() {
	}

    @Override
    public Class<LocalTime> getType() {
        return LocalTime.class;
    }

    @Override
    public void bind(PreparedStatement pstmt, int i, LocalTime val) throws SQLException {
		pstmt.setTime(i, Time.valueOf(val));
    }

	@Override
	public LocalTime fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var d = rs.getTime(i);
		return rs.wasNull() ? null : d.toLocalTime();
	}
}
