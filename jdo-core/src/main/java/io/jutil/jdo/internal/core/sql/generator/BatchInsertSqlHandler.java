package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;

/**
 * @author Jin Zheng
 * @since 2022-04-13
 */
public class BatchInsertSqlHandler extends AbstractSqlHandler {
	private final SqlHandler parameterSqlHandler;
    private final SqlHandler[] mapHandlers;

	public BatchInsertSqlHandler(SqlHandler parameterSqlHandler, SqlHandler... mapHandlers) {
		this.parameterSqlHandler = parameterSqlHandler;
		this.mapHandlers = mapHandlers;
	}

	@Override
    public void handle(SqlRequest request, SqlResponse response) {
		var config = request.getMetadata();
		var objectList = request.getArgs();
		var sqlItem = config.getSqlMetadata().getInsert();
		response.setSql(sqlItem.getSql());

		for (var object : objectList) {
			var req = SqlRequest.create(object, config, false);
			var resp = this.handle(config, req);
			for (var name : sqlItem.getParameterNameList()) {
				resp.addName(name);
			}
			parameterSqlHandler.handle(req, resp);
			response.addBatchParameter(resp.toParameterList());
		}
    }

	private SqlResponse handle(EntityMetadata config, SqlRequest request) {
		var response = new SqlResponse(config);
		for (var handler : mapHandlers) {
			handler.handle(request, response);
		}

		return response;
	}
}
