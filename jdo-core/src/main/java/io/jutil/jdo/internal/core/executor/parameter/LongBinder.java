package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class LongBinder implements ParameterBinder<Long> {
	public LongBinder() {
	}

    @Override
    public Class<Long> getType() {
        return Long.class;
    }

    @Override
    public void bind(PreparedStatement pstmt, int i, Long val) throws SQLException {
        pstmt.setLong(i, val);
    }
}
