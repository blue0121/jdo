package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class ByteBinder implements ParameterBinder<Byte> {
	public ByteBinder() {
	}

    @Override
    public Class<Byte> getType() {
        return Byte.class;
    }

    @Override
    public void bind(PreparedStatement pstmt, int i, Byte val) throws SQLException {
		pstmt.setByte(i, val);
    }

	@Override
	public Byte fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var n = rs.getByte(i);
		return rs.wasNull() ? null : n;
	}
}
