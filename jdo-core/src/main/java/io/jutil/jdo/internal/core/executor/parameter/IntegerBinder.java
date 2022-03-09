package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class IntegerBinder implements ParameterBinder<Integer> {
	public IntegerBinder() {
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Integer val) throws SQLException {
		pstmt.setInt(i, val);
	}
}
