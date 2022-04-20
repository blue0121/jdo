package io.jutil.jdo.internal.core.sql.map;

import io.jutil.jdo.core.exception.EntityFieldException;
import io.jutil.jdo.core.parser.IdConfig;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.internal.core.id.IdGenerator;
import io.jutil.jdo.internal.core.id.SnowflakeId;
import io.jutil.jdo.internal.core.sql.AbstractSqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.util.AssertUtil;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class InsertIdSqlHandler extends AbstractSqlHandler {
    private final SnowflakeId snowflakeId;

	public InsertIdSqlHandler(SnowflakeId snowflakeId) {
        AssertUtil.notNull(snowflakeId, "SnowflakeId");
        this.snowflakeId = snowflakeId;
	}

    @Override
    public void handle(SqlRequest request, SqlResponse response) {
        var idMap = request.getConfig().getIdMap();
        for (var entry : idMap.entrySet()) {
            var id = entry.getValue();
            var field = id.getFieldName();
            switch (id.getGeneratorType()) {
                case AUTO -> this.handleAuto(response, field, id.getIdType());
                case UUID -> this.handleUuid(response, field, id.getIdType());
                case INCREMENT -> this.handleIncrement(response, field, id.getIdType());
                case ASSIGNED -> this.handleAssigned(request, response, id);
                default -> throw new UnsupportedOperationException("不支持主键产生类型: " + id.getGeneratorType());
            }
        }
    }

    private void handleAuto(SqlResponse response, String field, IdType idType) {
        switch (idType) {
            case STRING -> response.putParam(field, IdGenerator.uuid32bit());
            default -> response.removeParam(field);
        }
    }

    private void handleUuid(SqlResponse response, String field, IdType idType) {
        switch (idType) {
            case STRING -> response.putParam(field, IdGenerator.uuid32bit());
            case LONG -> response.putParam(field, snowflakeId.nextId());
            default -> throw new UnsupportedOperationException("不支持主键类型: " + idType);
        }
    }

    private void handleIncrement(SqlResponse response, String field, IdType idType) {
        switch (idType) {
            case INT, LONG -> response.removeParam(field);
            default -> throw new UnsupportedOperationException("不支持主键类型: " + idType);
        }
    }

    private void handleAssigned(SqlRequest request, SqlResponse response, IdConfig id) {
        var map = request.getMap();
        Object value = null;
        if (map == null) {
            var beanField = id.getBeanField();
            value = beanField.getFieldValue(request.getTarget());
        } else {
            value = map.get(id.getFieldName());
        }
        if (this.isEmpty(value)) {
            throw new EntityFieldException(id.getFieldName(), "不能为空");
        }

        response.putParam(id.getFieldName(), value);
    }
}
