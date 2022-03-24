package io.jutil.jdo.internal.core.sql;

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
				response.putParam(entry.getKey(), entry.getValue());
			}
		}
	}
}
