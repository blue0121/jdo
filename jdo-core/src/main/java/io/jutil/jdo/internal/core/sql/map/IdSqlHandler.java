package io.jutil.jdo.internal.core.sql.map;

import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class IdSqlHandler extends AbstractSqlHandler {
	public IdSqlHandler() {
	}

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getConfig();
		var idMap = config.getIdMap();
		var map = request.getMap();
		if (map == null) {
			for (var entry : idMap.entrySet()) {
				this.putParam(request, response, entry.getValue());
			}
		} else {
			for (var entry : map.entrySet()) {
				if (idMap.containsKey(entry.getKey())) {
					response.putParam(entry.getKey(), entry.getValue());
				}
			}
		}
	}
}
