package io.jutil.jdo.internal.core.sql.parameter;

import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlParameter;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import lombok.NoArgsConstructor;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
@NoArgsConstructor
public class ParameterSqlHandler extends AbstractSqlHandler {


	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var fieldMap = request.getMetadata().getFieldMap();
		var list = response.toNameList();
		var map = response.toParamMap();
		for (var name : list) {
			var value = map.get(name);
			var field = fieldMap.get(name);
			var parameter = SqlParameter.create(field, value);
			response.addParameter(parameter);
		}
	}
}
