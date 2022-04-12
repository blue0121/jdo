package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.internal.core.id.SnowflakeId;
import io.jutil.jdo.internal.core.id.SnowflakeIdFactory;
import io.jutil.jdo.internal.core.parser.ConfigCache;
import io.jutil.jdo.internal.core.util.AssertUtil;
import io.jutil.jdo.internal.core.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class SqlHandlerFacade {
    private static Logger logger = LoggerFactory.getLogger(SqlHandlerFacade.class);

	private final ConfigCache configCache;
	private final SnowflakeId snowflakeId = SnowflakeIdFactory.getSingleSnowflakeId();
	private final EnumMap<SqlType, List<SqlHandle>> handlerMap = new EnumMap<>(SqlType.class);

	public SqlHandlerFacade(ConfigCache configCache) {
		this.configCache = configCache;
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

		var param = new ParamSqlHandler();

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

	public SqlResponse handle(SqlType type, Object object, boolean dynamic) {
		var config = configCache.loadEntityConfig(object.getClass());
		var request = SqlRequest.create(object, config, dynamic);
		return this.handle(type, request);
	}

	public SqlResponse handle(SqlType type, Object object, List<?> args) {
		var config = configCache.loadEntityConfig(object.getClass());
		var request = SqlRequest.create(object, args, config);
		return this.handle(type, request);
	}

	public SqlResponse handle(SqlType type, Class<?> clazz, List<?> args) {
		var config = configCache.loadEntityConfig(clazz);
		var request = SqlRequest.create(clazz, args, config);
		return this.handle(type, request);
	}

	public SqlResponse handle(SqlType type, Class<?> clazz, Map<String, ?> map) {
		var config = configCache.loadEntityConfig(clazz);
		var request = SqlRequest.create(map, config);
		return this.handle(type, request);
	}

	public SqlResponse handle(SqlType type, Class<?> clazz, String field, Map<String, ?> map) {
		var config = configCache.loadEntityConfig(clazz);
		var request = SqlRequest.create(field, map, config);
		return this.handle(type, request);
	}

	public SqlResponse handle(SqlType type, Class<?> clazz, Object id, Map<String, ?> map) {
		var config = configCache.loadEntityConfig(clazz);
		var idConfig = IdUtil.checkSingleId(config);
		Map<String, Object> param = new HashMap<>();
		for (var entry : map.entrySet()) {
			param.put(entry.getKey(), entry.getValue());
		}
		param.put(idConfig.getFieldName(), id);
		var request = SqlRequest.create(param, config);
		return this.handle(type, request);
	}

	private SqlResponse handle(SqlType type, SqlRequest request) {
		var response = new SqlResponse(request.getConfig());
		var handlers = this.getHandlers(type);
		for (var handler : handlers) {
			handler.handle(request, response);
		}
		return response;
	}

}
