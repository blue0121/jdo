package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.exception.EntityFieldException;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class UpdateIdSqlHandler extends AbstractSqlHandler {
	public UpdateIdSqlHandler() {
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
		var param = response.toParamMap();
		for (var entry : idMap.entrySet()) {
			var value = param.get(entry.getKey());
			if (this.isEmpty(value)) {
				throw new EntityFieldException(entry.getKey(), "不能为空");
			}
		}
	}
}
