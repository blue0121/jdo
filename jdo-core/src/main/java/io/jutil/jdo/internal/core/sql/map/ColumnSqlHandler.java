package io.jutil.jdo.internal.core.sql.map;

import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class ColumnSqlHandler extends AbstractSqlHandler {
	public ColumnSqlHandler() {
	}

    @Override
    public void handle(SqlRequest request, SqlResponse response) {
        var columnMap = request.getConfig().getColumnMap();
        var map = request.getMap();
        if (map == null) {
            for (var entry : columnMap.entrySet()) {
                this.putParam(request, response, entry.getValue());
            }
        } else {
            for (var entry : map.entrySet()) {
                response.putParam(entry.getKey(), entry.getValue());
            }
        }
    }
}
