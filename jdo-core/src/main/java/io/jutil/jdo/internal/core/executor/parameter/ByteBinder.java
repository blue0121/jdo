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
public class ByteBinder implements ParameterBinder<Byte> {


    @Override
    public Class<Byte> getType() {
        return Byte.class;
    }

	@Override
	public void bind(BindContext<Byte> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setByte(i, value);
	}

	@Override
	public Byte fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		var n = rs.getByte(i);
		return rs.wasNull() ? null : n;
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
