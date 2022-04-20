package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class LongBinder implements ParameterBinder<Long> {


    @Override
    public Class<Long> getType() {
        return Long.class;
    }

    @Override
    public void bind(PreparedStatement pstmt, int i, Long val) throws SQLException {
        pstmt.setLong(i, val);
    }

	@Override
	public Long fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var n = rs.getLong(i);
		return rs.wasNull() ? null : n;
	}
}
