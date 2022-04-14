package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlHandle;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.util.IdUtil;
import io.jutil.jdo.internal.core.util.VersionUtil;

/**
 * @author Jin Zheng
 * @since 2022-04-13
 */
public class BatchUpdateSqlHandler extends AbstractSqlHandler {
	private final SqlHandle parameterSqlHandler;
    private final SqlHandle[] mapHandlers;

	public BatchUpdateSqlHandler(SqlHandle parameterSqlHandler, SqlHandle... mapHandlers) {
		this.parameterSqlHandler = parameterSqlHandler;
		this.mapHandlers = mapHandlers;
	}

	@Override
    public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getConfig();
		var objectList = request.getArgs();
		var id = IdUtil.checkSingleId(config);
		var isForceVersion = VersionUtil.isForce(config);
		var sqlItem = isForceVersion ? config.getSqlConfig().getUpdateByIdAndVersion() : config.getSqlConfig().getUpdateById();
		response.setSql(sqlItem.getSql());
		response.setForceVersion(isForceVersion);

		for (var object : objectList) {
			var req = SqlRequest.create(object, config, false);
			var resp = this.handle(config, req);
			for (var name : sqlItem.getParamNameList()) {
				resp.addName(name);
			}
			parameterSqlHandler.handle(req, resp);

			response.addBatchParam(resp.toParamList());
		}
    }

	private SqlResponse handle(EntityConfig config, SqlRequest request) {
		var response = new SqlResponse(config);
		for (var handler : mapHandlers) {
			handler.handle(request, response);
		}

		return  response;
	}
}
