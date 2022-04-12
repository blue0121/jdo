package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.sql.generator.CountSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.DeleteSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.ExistSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.GetFieldSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.GetSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.IncSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.InsertSqlHandler;
import io.jutil.jdo.internal.core.sql.generator.UpdateSqlHandler;
import io.jutil.jdo.internal.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class SqlHandlerFactory {
    private static Logger logger = LoggerFactory.getLogger(SqlHandlerFactory.class);

    private final EnumMap<SqlType, SqlHandler> handlerMap = new EnumMap<>(SqlType.class);

    public SqlHandlerFactory() {
        handlerMap.put(SqlType.COUNT, new CountSqlHandler());
        handlerMap.put(SqlType.DELETE, new DeleteSqlHandler());
        handlerMap.put(SqlType.EXIST, new ExistSqlHandler());
        handlerMap.put(SqlType.GET_FIELD, new GetFieldSqlHandler());
        handlerMap.put(SqlType.GET, new GetSqlHandler());
        handlerMap.put(SqlType.INC, new IncSqlHandler());
        handlerMap.put(SqlType.INSERT, new InsertSqlHandler());
        handlerMap.put(SqlType.UPDATE, new UpdateSqlHandler());
    }

    public SqlItem handle(SqlType type, EntityConfig config, Map<String, ?> map) {
        var handler = this.getHandler(type);
        var param = new SqlParam();
        param.setEntityConfig(config);
        param.setMap(map);
        return handler.sql(param);
    }

    public SqlItem handle(SqlType type, EntityConfig config, String field, Map<String, ?> map) {
        var handler = this.getHandler(type);
        var param = new SqlParam();
        param.setEntityConfig(config);
        param.setField(field);
        param.setMap(map);
        return handler.sql(param);
    }

    public SqlItem handle(SqlType type, EntityConfig config, Map<String, ?> map, List<String> args) {
        var handler = this.getHandler(type);
        var param = new SqlParam();
        param.setEntityConfig(config);
        param.setMap(map);
        param.setArgs(args);
        return handler.sql(param);
    }

    private SqlHandler getHandler(SqlType type) {
        AssertUtil.notNull(type, "SqlType");
        var handler = handlerMap.get(type);
        if (handler == null) {
            throw new UnsupportedOperationException("未知 SqlType: " + type);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("找到 [{}] 处理器: {}", type.name(), handler.getClass().getSimpleName());
        }
        return handler;
    }

}
