package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author Jin Zheng
 * @since 2022-08-09
 */
@NoArgsConstructor
public class InstantBinder implements ParameterBinder<Instant> {

    @Override
    public Class<Instant> getType() {
        return Instant.class;
    }

    @Override
    public void bind(BindContext<Instant> context) throws SQLException {
        var pstmt = context.getPreparedStatement();
        var i = context.getIndex();
        var value = context.getValue();
        pstmt.setTimestamp(i, Timestamp.from(value));
    }

    @Override
    public Instant fetch(FetchContext context) throws SQLException {
        var rs = context.getResultSet();
        var i = context.getIndex();
        var d = rs.getTimestamp(i);
        return rs.wasNull() ? null : d.toInstant();
    }
}
