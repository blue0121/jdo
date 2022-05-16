package io.jutil.jdo.internal.core.sql.map;

import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import lombok.NoArgsConstructor;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
@NoArgsConstructor
public class ColumnSqlHandler extends AbstractSqlHandler {


    @Override
    public void handle(SqlRequest request, SqlResponse response) {
        var config = request.getMetadata();
        var columnMap = config.getColumnMap();
        var map = request.getMap();
        if (map == null) {
            for (var entry : columnMap.entrySet()) {
                this.putParam(request, response, entry.getValue());
            }
        } else {
            for (var entry : map.entrySet()) {
                if (columnMap.containsKey(entry.getKey())) {
                    response.putParam(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
