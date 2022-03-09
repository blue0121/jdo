package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class DateBinder implements ParameterBinder<Date> {
	public DateBinder() {
	}

    @Override
    public Class<Date> getType() {
        return Date.class;
    }

    @Override
    public void bind(PreparedStatement pstmt, int i, Date val) throws SQLException {
		var times = val.getTime();
		pstmt.setTimestamp(i, new Timestamp(times));
    }
}
