package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

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
	public void bind(BindContext<Long> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setLong(i, value);
	}

	@Override
	public Long fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		var n = rs.getLong(i);
		return rs.wasNull() ? null : n;
	}

}
