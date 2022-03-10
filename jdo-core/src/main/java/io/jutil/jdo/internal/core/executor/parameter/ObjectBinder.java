package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class ObjectBinder implements ParameterBinder<Object> {
	public ObjectBinder() {
	}

	@Override
	public Class<Object> getType() {
		return Object.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Object val) throws SQLException {
		pstmt.setObject(i, val);
	}

	@Override
	public Object fetch(ResultSet rs, int i) throws SQLException {
		return rs.getObject(i);
	}
}
