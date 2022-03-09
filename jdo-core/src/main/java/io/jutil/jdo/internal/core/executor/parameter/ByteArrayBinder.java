package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class ByteArrayBinder implements ParameterBinder<byte[]> {
	public ByteArrayBinder() {
	}

	@Override
	public Class<byte[]> getType() {
		return byte[].class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, byte[] val) throws SQLException {
		pstmt.setBytes(i, val);
	}
}
