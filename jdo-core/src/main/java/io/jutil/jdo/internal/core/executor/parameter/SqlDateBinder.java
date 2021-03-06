package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class SqlDateBinder implements ParameterBinder<Date> {


	@Override
	public Class<Date> getType() {
		return Date.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Date val) throws SQLException {
		pstmt.setDate(i, val);
	}

	@Override
	public Date fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		return rs.getDate(i);
	}
}
