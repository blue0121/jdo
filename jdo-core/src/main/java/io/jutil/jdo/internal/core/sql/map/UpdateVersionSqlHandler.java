package io.jutil.jdo.internal.core.sql.map;

import io.jutil.jdo.core.exception.VersionException;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class UpdateVersionSqlHandler extends AbstractSqlHandler {
	public UpdateVersionSqlHandler() {
	}

	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		var version = request.getConfig().getVersionConfig();
		if (version == null) {
			return;
		}

		response.setForceVersion(version.isForce());
		var map = request.getMap();
		if (map == null) {
			var beanField = version.getBeanField();
			var value = beanField.getFieldValue(request.getTarget());
			if (value != null) {
				response.putParam(version.getFieldName(), value);
				return;
			}
		} else {
			var field = version.getFieldName();
			var value = map.get(field);
			if (value != null) {
				response.putParam(field, value);
				return;
			}
		}
		if (version.isForce()) {
			throw new VersionException(request.getClazz(), "缺少版本号");
		}
	}
}
