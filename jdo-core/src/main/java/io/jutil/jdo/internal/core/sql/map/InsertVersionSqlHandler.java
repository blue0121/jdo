package io.jutil.jdo.internal.core.sql.map;

import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class InsertVersionSqlHandler extends AbstractSqlHandler {
	public InsertVersionSqlHandler() {
	}

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var version = request.getConfig().getVersionConfig();
		if (version == null) {
			return;
		}

		var map = request.getMap();
		Object value = null;
		if (map == null) {
			var beanField = version.getBeanField();
			value = beanField.getFieldValue(request.getTarget());
			if (value == null) {
				value = version.getDefaultValue();
				beanField.setFieldValue(request.getTarget(), value);
			}
		} else {
			var field = version.getFieldName();
			value = map.get(field);
			if (value == null) {
				value = version.getDefaultValue();
			}
		}
		response.putParam(version.getFieldName(), value);
	}
}
