package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class ByteArrayBinder implements ParameterBinder<byte[]> {


	@Override
	public Class<byte[]> getType() {
		return byte[].class;
	}

	@Override
	public void bind(BindContext<byte[]> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setBytes(i, value);
	}

	@Override
	public byte[] fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		return rs.getBytes(i);
	}

}
