package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.internal.core.id.SnowflakeId;
import io.jutil.jdo.internal.core.id.SnowflakeIdFactory;
import io.jutil.jdo.internal.core.sql.generator.BatchInsertSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.BatchUpdateSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.CountSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.DeleteBySqlHandler;
import io.jutil.jdo.internal.core.sql.generator.DeleteSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.ExistSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.GetFieldSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.GetIdSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.GetSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.IncSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.InsertSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.UpdateSqlHandler;
import io.jutil.jdo.internal.core.sql.map.ColumnSqlHandler;
import io.jutil.jdo.internal.core.sql.map.IdSqlHandler;
import io.jutil.jdo.internal.core.sql.map.InsertIdSqlHandler;
import io.jutil.jdo.internal.core.sql.map.InsertVersionSqlHandler;
import io.jutil.jdo.internal.core.sql.map.UpdateIdSqlHandler;
import io.jutil.jdo.internal.core.sql.map.UpdateVersionSqlHandler;
import io.jutil.jdo.internal.core.sql.map.VersionSqlHandler;
import io.jutil.jdo.internal.core.sql.parameter.ParameterSqlHandler;
import io.jutil.jdo.internal.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class SqlHandlerFacade {
    private static Logger logger = LoggerFactory.getLogger(SqlHandlerFacade.class);

	private final SnowflakeId snowflakeId = SnowflakeIdFactory.getSingleSnowflakeId();
	private final EnumMap<SqlType, List<SqlHandle>> handlerMap = new EnumMap<>(SqlType.class);

	public SqlHandlerFacade() {
		this.init();
	}

	private void init() {
		var insertId = new InsertIdSqlHandler(snowflakeId);
		var updateId = new UpdateIdSqlHandler();
		var id = new IdSqlHandler();
		var column = new ColumnSqlHandler();
		var insertVersion = new InsertVersionSqlHandler();
		var updateVersion = new UpdateVersionSqlHandler();
		var version = new VersionSqlHandler();

		var param = new ParameterSqlHandler();

		this.addHandler(SqlType.INSERT, insertId, column, insertVersion, new InsertSqlHandler(), param);
		this.addHandler(SqlType.UPDATE, updateId, column, updateVersion, new UpdateSqlHandler(), param);
		this.addHandler(SqlType.INC, updateId, column, version, new IncSqlHandler(), param);
		this.addHandler(SqlType.COUNT, id, column, version, new CountSqlHandler(), param);
		this.addHandler(SqlType.GET, id, column, version, new GetSqlHandler(), param);
		this.addHandler(SqlType.GET_FIELD, id, column, version, new GetFieldSqlHandler(), param);
		this.addHandler(SqlType.EXIST, id, column, version, new ExistSqlHandler(), param);
		this.addHandler(SqlType.DELETE_BY, id, column, version, new DeleteBySqlHandler(), param);
		this.addHandler(SqlType.DELETE, new DeleteSqlHandler());
		this.addHandler(SqlType.GET_ID, new GetIdSqlHandler());
		this.addHandler(SqlType.BATCH_INSERT, new BatchInsertSqlHandler(param, insertId, column, insertVersion));
		this.addHandler(SqlType.BATCH_UPDATE, new BatchUpdateSqlHandler(param, updateId, column, updateVersion));
	}

	private void addHandler(SqlType type, SqlHandle...handlers) {
		var list = List.of(handlers);
		handlerMap.put(type, list);
	}

	private List<SqlHandle> getHandlers(SqlType type) {
		AssertUtil.notNull(type, "SqlType");
		var handlers = handlerMap.get(type);
		if (handlers == null || handlers.isEmpty()) {
			throw new UnsupportedOperationException("未知 SqlType: " + type);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("找到 [{}] 处理器: {}", type.name(), handlers.getClass().getSimpleName());
		}
		return handlers;
	}

	public SqlResponse handle(SqlType type, SqlRequest request) {
		var response = new SqlResponse(request.getConfig());
		var handlers = this.getHandlers(type);
		for (var handler : handlers) {
			handler.handle(request, response);
		}
		return response;
	}

}
