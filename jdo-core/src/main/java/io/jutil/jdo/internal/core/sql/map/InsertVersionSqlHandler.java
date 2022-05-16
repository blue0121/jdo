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
public class InsertVersionSqlHandler extends AbstractSqlHandler {


	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var version = request.getMetadata().getVersionMetadata();
		if (version == null) {
			return;
		}

		response.setForceVersion(version.isForce());
		var map = request.getMap();
		Object value = null;
		if (map == null) {
			var beanField = version.getFieldOperation();
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
