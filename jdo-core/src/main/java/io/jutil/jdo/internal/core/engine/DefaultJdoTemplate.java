package io.jutil.jdo.internal.core.engine;

import io.jutil.jdo.core.annotation.LockModeType;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.exception.VersionException;
import io.jutil.jdo.core.parser.ConfigType;
import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.executor.ConnectionFactory;
import io.jutil.jdo.internal.core.executor.GenerateKeyHolder;
import io.jutil.jdo.internal.core.executor.KeyHolder;
import io.jutil.jdo.internal.core.parser.ConfigCache;
import io.jutil.jdo.internal.core.parser.ParserFactory;
import io.jutil.jdo.internal.core.sql.SqlHandlerFactory;
import io.jutil.jdo.internal.core.sql.SqlType;
import io.jutil.jdo.internal.core.util.AssertUtil;
import io.jutil.jdo.internal.core.util.IdUtil;
import io.jutil.jdo.internal.core.util.ObjectUtil;
import io.jutil.jdo.internal.core.util.ParamUtil;
import io.jutil.jdo.internal.core.util.StringUtil;
import io.jutil.jdo.internal.core.util.VersionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
	private final ConfigCache configCache;
	private final ConnectionFactory connectionFactory;
	private final SqlHandlerFactory sqlHandlerFactory;

	public DefaultJdoTemplate(ParserFactory parserFactory, ConnectionFactory connectionFactory) {
		this.dialect = parserFactory.getDialect();
		this.configCache = parserFactory.getConfigCache();
		this.connectionFactory = connectionFactory;
		this.sqlHandlerFactory = new SqlHandlerFactory();
	}

	@Override
	public int save(Object object, boolean dynamic) {
		AssertUtil.notNull(object, "Object");
		var config = configCache.loadEntityConfig(object.getClass());
		var param = ObjectUtil.toMap(object, config, dynamic);
		IdUtil.generateId(param, config.getIdMap());
		var sqlItem = dynamic ? sqlHandlerFactory.handle(SqlType.INSERT, config, param) :
				config.getSqlConfig().getInsert();
		var sql = sqlItem.getSql();
		var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), dynamic);
		KeyHolder holder = new GenerateKeyHolder();
		int count = connectionFactory.execute(sql, paramList, holder);
		IdUtil.setId(holder, object, config);
		return count;
	}

	@Override
	public int saveObject(Class<?> clazz, Map<String, ?> param) {
		var config = configCache.loadEntityConfig(clazz);
		var map = ObjectUtil.generateMap(param, config);
		var sqlItem = sqlHandlerFactory.handle(SqlType.INSERT, config, map);
		var sql = sqlItem.getSql();
		var paramList = ParamUtil.toParamList(map, sqlItem.getParamNameList(), false);
		return connectionFactory.execute(sql, paramList);
	}

	@Override
	public int[] saveList(List<?> objectList) {
		AssertUtil.notEmpty(objectList, "ObjectList");
		var config = configCache.loadEntityConfig(objectList.get(0).getClass());
		var sqlItem = config.getSqlConfig().getInsert();
		var sql = sqlItem.getSql();
		List<List<?>> batchList = new ArrayList<>();
		for (var object : objectList) {
			var param = ObjectUtil.toMap(object, config, false);
			IdUtil.generateId(param, config.getIdMap());
			var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), false);
			batchList.add(paramList);
		}
		KeyHolder holder = new GenerateKeyHolder();
		int[] count = connectionFactory.executeBatch(sql, batchList, holder);
		IdUtil.setId(holder, objectList, config);
		return count;
	}

	@Override
	public int update(Object object, boolean dynamic) {
		AssertUtil.notNull(object, "Object");
		var config = configCache.loadEntityConfig(object.getClass());
		var param = ObjectUtil.toMap(object, config, dynamic);
		var sqlConfig = config.getSqlConfig();
		var isForceVer = VersionUtil.isForce(config);
		SqlItem sqlItem = null;
		if (dynamic) {
			sqlItem = sqlHandlerFactory.handle(SqlType.UPDATE, config, param);
		} else if (isForceVer) {
			sqlItem = sqlConfig.getUpdateByIdAndVersion();
		} else {
			sqlItem = sqlConfig.getUpdateById();
		}
		var sql = sqlItem.getSql();
		var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), dynamic);
		int count = connectionFactory.execute(sql, paramList);
		if (isForceVer && count <= 0) {
			throw new VersionException(object.getClass());
		}
		return count;
	}

	@Override
	public int updateObject(Class<?> clazz, Object id, Map<String, ?> param) {
		var config = configCache.loadEntityConfig(clazz);
		var idConfig = IdUtil.checkSingleId(config);
		Map<String, Object> map = new HashMap<>(param);
		map.put(idConfig.getFieldName(), id);
		var sqlItem = sqlHandlerFactory.handle(SqlType.UPDATE, config, map);
		var sql = sqlItem.getSql();
		var isForceVer = VersionUtil.isForce(config);
		var paramList = ParamUtil.toParamList(map, sqlItem.getParamNameList(), false);
		int count = connectionFactory.execute(sql, paramList);
		if (isForceVer && count <= 0) {
			throw new VersionException(clazz);
		}
		return count;
	}

	@Override
	public int[] updateList(List<?> objectList) {
		AssertUtil.notEmpty(objectList, "ObjectList");
		var clazz = objectList.get(0).getClass();
		var config = configCache.loadEntityConfig(clazz);
		var sqlConfig = config.getSqlConfig();
		var isForceVer = VersionUtil.isForce(config);
		var sqlItem = isForceVer ? sqlConfig.getUpdateByIdAndVersion() : sqlConfig.getUpdateById();
		var sql = sqlItem.getSql();
		List<List<?>> batchList = new ArrayList<>();
		for (var object : objectList) {
			var param = ObjectUtil.toMap(object, config, false);
			IdUtil.generateId(param, config.getIdMap());
			var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), false);
			batchList.add(paramList);
		}
		int[] count = connectionFactory.executeBatch(sql, batchList);
		if (isForceVer) {
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
		var config = configCache.loadEntityConfig(clazz);
		var idConfig = IdUtil.checkSingleId(config);
		var sqlItem = sqlHandlerFactory.handle(SqlType.INC, config, param);
		var sql = sqlItem.getSql();
		Map<String, Object> map = new HashMap<>(param);
		map.put(idConfig.getFieldName(), id);
		var paramList = ParamUtil.toParamList(map, sqlItem.getParamNameList(), false);
		return connectionFactory.execute(sql, paramList);
	}

	@Override
	public int deleteId(Class<?> clazz, Object id) {
		var config = configCache.loadEntityConfig(clazz);
		IdUtil.checkSingleId(config);
		var sqlItem = config.getSqlConfig().getDeleteById();
		var sql = sqlItem.getSql();
		return connectionFactory.execute(sql, List.of(id));
	}

	@Override
	public <K, T> int deleteIdList(Class<T> clazz, List<K> idList) {
		var config = configCache.loadEntityConfig(clazz);
		var id = IdUtil.checkSingleId(config);
		var sqlItem = config.getSqlConfig().getDeleteByIdList();
		var sql = String.format(sqlItem.getSql(), StringUtil.repeat("?", idList.size(), ","));
		return connectionFactory.execute(sql, idList);
	}

	@Override
	public <T> int deleteBy(Class<T> clazz, Map<String, ?> param) {
		var config = configCache.loadEntityConfig(clazz);
		var sqlItem = sqlHandlerFactory.handle(SqlType.DELETE, config, param);
		var sql = sqlItem.getSql();
		var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), false);
		return connectionFactory.execute(sql, paramList);
	}

	@Override
	public <T> T get(Class<T> clazz, Object id, LockModeType type) {
		var config = configCache.loadEntityConfig(clazz);
		IdUtil.checkSingleId(config);
		var sqlItem = config.getSqlConfig().getSelectById();
		var sql = dialect.lock(dialect.page(sqlItem.getSql(), 0, 1), type);
		List<T> list = connectionFactory.query(config, sql, List.of(id));
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, T> Map<K, T> getList(Class<T> clazz, List<K> idList) {
		var config = configCache.loadEntityConfig(clazz);
		var id = IdUtil.checkSingleId(config);
		var sqlItem = config.getSqlConfig().getSelectByIdList();
		var sql = String.format(sqlItem.getSql(), StringUtil.repeat("?", idList.size(), ","));
		List<T> list = connectionFactory.query(config, sql, idList);
		if (list.isEmpty()) {
			return Map.of();
		}

		Map<K, T> map = new HashMap<>();
		var idField = id.getBeanField();
		for (var o : list) {
			map.put((K)idField.getFieldValue(o), o);
		}
		return map;
	}

	@Override
	public <T> T getField(Class<?> clazz, Class<T> target, String field, Map<String, ?> param) {
		var config = configCache.loadEntityConfig(clazz);
		var sqlItem = sqlHandlerFactory.handle(SqlType.GET_FIELD, config, field, param);
		var sql = dialect.getOne(sqlItem.getSql());
		var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), false);
		List<T> list = connectionFactory.query(target, sql, paramList);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public <T> T getObject(Class<T> clazz, Map<String, ?> param) {
		var config = configCache.loadEntityConfig(clazz);
		var sqlItem = sqlHandlerFactory.handle(SqlType.GET, config, param);
		var sql = dialect.getOne(sqlItem.getSql());
		var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), false);
		List<T> list = connectionFactory.query(config, sql, paramList);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public boolean exist(Object object, String... names) {
		AssertUtil.notNull(object, "Object");
		var nameList = Arrays.asList(names);
		var config = configCache.loadEntityConfig(object.getClass());
		var param = ObjectUtil.toMap(object, config, false);
		var sqlItem = sqlHandlerFactory.handle(SqlType.EXIST, config, param, nameList);
		var sql = sqlItem.getSql();
		var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), false);
		var list = connectionFactory.query(Integer.class, sql, paramList);
		if (list.isEmpty()) {
			return false;
		}
		int count = (Integer) list.get(0);
		return count > 0;
	}

	@Override
	public int count(Class<?> clazz, Map<String, ?> param) {
		var config = configCache.loadEntityConfig(clazz);
		var sqlItem = sqlHandlerFactory.handle(SqlType.COUNT, config, param);
		var sql = sqlItem.getSql();
		var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), false);
		var list = connectionFactory.query(Integer.class, sql, paramList);
		if (list.isEmpty()) {
			logger.warn("No result");
			return -1;
		}
		return (Integer) list.get(0);
	}

	@Override
	public <T> T getObject(Class<T> clazz, String sql, List<?> paramList) {
		List<T> list = connectionFactory.query(clazz, sql, paramList);
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
		return connectionFactory.query(clazz, sql, paramList);
	}

	@Override
	public <T> List<T> listField(Class<?> clazz, Class<T> target, String field, Map<String, ?> param) {
		var config = configCache.loadEntityConfig(clazz);
		var sqlItem = sqlHandlerFactory.handle(SqlType.GET_FIELD, config, field, param);
		var sql = dialect.getOne(sqlItem.getSql());
		var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), false);
		return connectionFactory.query(target, sql, paramList);
	}

	@Override
	public <T> List<T> listObject(Class<T> clazz, Map<String, ?> param) {
		var config = configCache.loadEntityConfig(clazz);
		var sqlItem = sqlHandlerFactory.handle(SqlType.GET, config, param);
		var sql = dialect.getOne(sqlItem.getSql());
		var paramList = ParamUtil.toParamList(param, sqlItem.getParamNameList(), false);
		return connectionFactory.query(config, sql, paramList);
	}

    @Override
    public int execute(String sql) {
        return connectionFactory.execute(sql);
    }

	@Override
	public EntityConfig checkEntityConfig(Class<?> clazz) {
		boolean existEntity = configCache.exist(clazz, ConfigType.ENTITY);
		boolean existMapper = configCache.exist(clazz, ConfigType.MAPPER);
		if (!existEntity) {
			if (existMapper) {
				throw new JdbcException(clazz.getName() + " 需要覆写 select(), selectCount() 和 orderBy() 方法");
			}
			else {
				throw new JdbcException(clazz.getName() + " 缺少 @Entity 或 @Mapper 注解");
			}
		}
		return configCache.loadEntityConfig(clazz);
	}

}
