package io.jutil.jdo.internal.core.sql.map;

import io.jutil.jdo.core.exception.VersionException;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import lombok.NoArgsConstructor;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
@NoArgsConstructor
public class UpdateVersionSqlHandler extends VersionSqlHandler {


	@Override
	public void handle(SqlRequest request, SqlResponse response) {
		super.handle(request, response);

		var version = request.getConfig().getVersionConfig();
		if (version == null || !version.isForce()) {
			return;
		}

		var object = response.toParamMap().get(version.getFieldName());
		if (object == null) {
			throw new VersionException(request.getClazz(), "缺少版本号");
		}
	}
}
