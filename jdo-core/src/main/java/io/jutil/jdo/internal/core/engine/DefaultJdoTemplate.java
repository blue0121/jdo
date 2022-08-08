package io.jutil.jdo.internal.core.engine;

import io.jutil.jdo.core.annotation.LockModeType;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.exception.VersionException;
import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.core.parser.MetadataType;
import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.executor.ExecuteContext;
import io.jutil.jdo.internal.core.executor.GenerateKeyHolder;
import io.jutil.jdo.internal.core.executor.KeyHolder;
import io.jutil.jdo.internal.core.executor.SqlExecutor;
import io.jutil.jdo.internal.core.parser.MetadataCache;
import io.jutil.jdo.internal.core.parser.ParserFacade;
import io.jutil.jdo.internal.core.sql.SqlHandlerFacade;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlType;
import io.jutil.jdo.internal.core.util.AssertUtil;
import io.jutil.jdo.internal.core.util.IdUtil;
import io.jutil.jdo.internal.core.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public class DefaultJdoTemplate implements JdoTemplate {
	private static Logger logger = LoggerFactory.getLogger(DefaultJdoTemplate.class);

	private final Dialect dialect;
	private final MetadataCache configCache;
	private final SqlHandlerFacade sqlHandlerFacade;
	private final SqlExecutor sqlExecutor;

	public DefaultJdoTemplate(ParserFacade parserFactory, SqlExecutor sqlExecutor) {
		this.dialect = parserFactory.getDialect();
		this.configCache = parserFactory.getMetadataCache();
		this.sqlHandlerFacade = new SqlHandlerFacade();
		this.sqlExecutor = sqlExecutor;
	}

	@Override
	public int insert(Object object, boolean dynamic) {
		AssertUtil.notNull(object, "对象");

		var config = configCache.loadEntityMetadata(object.getClass());
		var request = SqlRequest.create(object, config, dynamic);
		var response = sqlHandlerFacade.handle(SqlType.INSERT, request);
		var context = ExecuteContext.create(response);

		KeyHolder holder = new GenerateKeyHolder();
		int count = sqlExecutor.execute(context, holder);
		IdUtil.setId(holder, object, response.getMetadata());
		return count;
	}

	@Override
	public int insertObject(Class<?> clazz, Map<String, ?> param) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notEmpty(param, "Map");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(param, config);
		var response = sqlHandlerFacade.handle(SqlType.INSERT, request);
		var context = ExecuteContext.create(response);

		return sqlExecutor.execute(context);
	}

	@Override
	public int[] insertList(List<?> objectList) {
		AssertUtil.notEmpty(objectList, "ObjectList");

		var config = configCache.loadEntityMetadata(objectList.get(0).getClass());
		var request = SqlRequest.createForBatch(objectList, config);
		var response = sqlHandlerFacade.handle(SqlType.BATCH_INSERT, request);
		var context = ExecuteContext.create(response);

		KeyHolder holder = new GenerateKeyHolder();
		int[] count = sqlExecutor.executeBatch(context, holder);
		IdUtil.setId(holder, objectList, config);
		return count;
	}

	@Override
	public int update(Object object, boolean dynamic) {
		AssertUtil.notNull(object, "Object");

		var config = configCache.loadEntityMetadata(object.getClass());
		var request = SqlRequest.create(object, config, dynamic);
		var response = sqlHandlerFacade.handle(SqlType.UPDATE, request);
		var context = ExecuteContext.create(response);

		int count = sqlExecutor.execute(context);
		if (response.isForceVersion() && count <= 0) {
			throw new VersionException(object.getClass());
		}
		return count;
	}

	@Override
	public int updateObject(Class<?> clazz, Object id, Map<String, ?> param) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notNull(id, "主键");
		AssertUtil.notEmpty(param, "Map");

		var config = configCache.loadEntityMetadata(clazz);
		var idConfig = IdUtil.checkSingleId(config);
		var map = MapUtil.merge(param, idConfig.getFieldName(), id);
		var request = SqlRequest.create(map, config);
		var response = sqlHandlerFacade.handle(SqlType.UPDATE, request);
		var context = ExecuteContext.create(response);

		int count = sqlExecutor.execute(context);
		if (response.isForceVersion() && count <= 0) {
			throw new VersionException(clazz);
		}
		return count;
	}

	@Override
	public int[] updateList(List<?> objectList) {
		AssertUtil.notEmpty(objectList, "ObjectList");

		var clazz = objectList.get(0).getClass();
		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.createForBatch(objectList, config);
		var response = sqlHandlerFacade.handle(SqlType.BATCH_UPDATE, request);
		var context = ExecuteContext.create(response);

		int[] count = sqlExecutor.executeBatch(context);
		if (response.isForceVersion()) {
			for (int c : count) {
				if (c <= 0) {
					throw new VersionException(clazz);
				}
			}
		}
		return count;
	}

	@Override
	public int inc(Class<?> clazz, Object id, Map<String, ? extends Number> param) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notNull(id, "主键");
		AssertUtil.notEmpty(param, "Map");

		var config = configCache.loadEntityMetadata(clazz);
		var idConfig = IdUtil.checkSingleId(config);
		var map = MapUtil.merge(param, idConfig.getFieldName(), id);
		var request = SqlRequest.create(map, config);
		var response = sqlHandlerFacade.handle(SqlType.INC, request);
		var context = ExecuteContext.create(response);

		return sqlExecutor.execute(context);
	}

	@Override
	public int deleteId(Class<?> clazz, Object id) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notNull(id, "Id");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(clazz, List.of(id), config);
		var response = sqlHandlerFacade.handle(SqlType.DELETE, request);
		var context = ExecuteContext.create(response);

		return sqlExecutor.execute(context);
	}

	@Override
	public <K, T> int deleteIdList(Class<T> clazz, List<K> idList) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notEmpty(idList, "Id列表");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(clazz, idList, config);
		var response = sqlHandlerFacade.handle(SqlType.DELETE, request);
		var context = ExecuteContext.create(response);

		return sqlExecutor.execute(context);
	}

	@Override
	public <T> int deleteBy(Class<T> clazz, Map<String, ?> param) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notEmpty(param, "Map");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(param, config);
		var response = sqlHandlerFacade.handle(SqlType.DELETE_BY, request);
		var context = ExecuteContext.create(response);

		return sqlExecutor.execute(context);
	}

	@Override
	public <T> T get(Class<T> clazz, Object id, LockModeType type) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notNull(id, "Id");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(clazz, List.of(id), config);
		var response = sqlHandlerFacade.handle(SqlType.GET_ID, request);

		var sql = dialect.lock(dialect.getOne(response.getSql()), type);
		var context = ExecuteContext.create(sql, response);
		List<T> list = sqlExecutor.query(clazz, context);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, T> Map<K, T> getList(Class<T> clazz, List<K> idList) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notEmpty(idList, "Id列表");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(clazz, idList, config);
		var response = sqlHandlerFacade.handle(SqlType.GET_ID, request);
		var context = ExecuteContext.create(response);

		List<T> list = sqlExecutor.query(clazz, context);
		if (list.isEmpty()) {
			return Map.of();
		}

		var id = response.getMetadata().getIdMetadata();
		Map<K, T> map = new HashMap<>();
		var idField = id.getFieldOperation();
		for (var o : list) {
			map.put((K)idField.getFieldValue(o), o);
		}
		return map;
	}

	@Override
	public <T> T getField(Class<?> clazz, Class<T> target, String field, Map<String, ?> param) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notNull(target, "目标类型");
		AssertUtil.notEmpty(field, "字段");
		AssertUtil.notEmpty(param, "Map");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(field, param, config);
		var response = sqlHandlerFacade.handle(SqlType.GET_FIELD, request);
		var context = ExecuteContext.create(response);

		List<T> list = sqlExecutor.query(target, context);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public <T> T getObject(Class<T> clazz, Map<String, ?> param) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notEmpty(param, "Map");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(param, config);
		var response = sqlHandlerFacade.handle(SqlType.GET, request);
		var context = ExecuteContext.create(response);

		List<T> list = sqlExecutor.query(clazz, context);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public boolean exist(Object object, String... names) {
		AssertUtil.notNull(object, "Object");

		var config = configCache.loadEntityMetadata(object.getClass());
		var request = SqlRequest.create(object, Arrays.asList(names), config);
		var response = sqlHandlerFacade.handle(SqlType.EXIST, request);
		var context = ExecuteContext.create(response);

		var list = sqlExecutor.query(Integer.class, context);
		if (list.isEmpty()) {
			return false;
		}
		int count = list.get(0);
		return count > 0;
	}

	@Override
	public int count(Class<?> clazz, Map<String, ?> param) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notEmpty(param, "Map");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(param, config);
		var response = sqlHandlerFacade.handle(SqlType.COUNT, request);
		var context = ExecuteContext.create(response);

		var list = sqlExecutor.query(Integer.class, context);
		if (list.isEmpty()) {
			logger.warn("No result");
			return -1;
		}
		return list.get(0);
	}

	@Override
	public <T> T getObject(Class<T> clazz, String sql, List<?> paramList) {
		List<T> list = sqlExecutor.query(clazz, sql, paramList);
		if (list.isEmpty()) {
			throw null;
		}
		return list.get(0);
	}

	@Override
	public <T> List<T> list(Class<T> clazz, String sql, int start, int size) {
		sql = dialect.page(sql, start, size);
		return this.list(clazz, sql, null);
	}

	@Override
	public <T> List<T> list(Class<T> clazz, String sql, List<?> paramList, int start, int size) {
		sql = dialect.page(sql, start, size);
		return this.list(clazz, sql, paramList);
	}

	@Override
	public <T> List<T> list(Class<T> clazz, String sql, List<?> paramList) {
		return sqlExecutor.query(clazz, sql, paramList);
	}

	@Override
	public <T> List<T> listField(Class<?> clazz, Class<T> target, String field, Map<String, ?> param) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notNull(target, "目标类型");
		AssertUtil.notEmpty(field, "字段");
		AssertUtil.notEmpty(param, "Map");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(field, param, config);
		var response = sqlHandlerFacade.handle(SqlType.GET_FIELD, request);

		return sqlExecutor.query(target, response.getSql(), response.toParamList());
	}

	@Override
	public <T> List<T> listObject(Class<T> clazz, Map<String, ?> param) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notEmpty(param, "Map");

		var config = configCache.loadEntityMetadata(clazz);
		var request = SqlRequest.create(param, config);
		var response = sqlHandlerFacade.handle(SqlType.GET, request);

		return sqlExecutor.query(clazz, response.getSql(), response.toParamList());
	}

    @Override
    public int execute(String sql) {
        return sqlExecutor.execute(sql);
    }

	@Override
	public EntityMetadata checkEntityConfig(Class<?> clazz) {
		boolean existEntity = configCache.exist(clazz, MetadataType.ENTITY);
		boolean existMapper = configCache.exist(clazz, MetadataType.MAPPER);
		if (!existEntity) {
			if (existMapper) {
				throw new JdbcException(clazz.getName() + " 需要覆写 select(), selectCount() 和 orderBy() 方法");
			}
			else {
				throw new JdbcException(clazz.getName() + " 缺少 @Entity 或 @Mapper 注解");
			}
		}
		return configCache.loadEntityMetadata(clazz);
	}

}
