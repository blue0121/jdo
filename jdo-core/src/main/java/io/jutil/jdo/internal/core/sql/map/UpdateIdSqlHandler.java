package io.jutil.jdo.internal.core.sql.map;

import io.jutil.jdo.core.exception.EntityFieldException;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import lombok.NoArgsConstructor;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
@NoArgsConstructor
public class UpdateIdSqlHandler extends IdSqlHandler {


	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		super.handle(request, response);

		var config = request.getConfig();
		var idMap = config.getIdMap();
		var param = response.toParamMap();
		for (var entry : idMap.entrySet()) {
			var value = param.get(entry.getKey());
			if (this.isEmpty(value)) {
				throw new EntityFieldException(entry.getKey(), "不能为空");
			}
		}
	}
}
