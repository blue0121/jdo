package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser.IdConfig;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.internal.core.id.IdGenerator;
import io.jutil.jdo.internal.core.id.SnowflakeId;

/**
 * @author Jin Zheng
 * @since 2022-03-23
 */
public class InsertIdSqlHandler extends AbstractSqlHandler {
    private final SnowflakeId snowflakeId;

	public InsertIdSqlHandler(SnowflakeId snowflakeId) {
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
        if (map == null) {
            this.putParam(request, response, id);
        } else {
            var value = map.get(id.getFieldName());
            response.putParam(id.getFieldName(), value);
        }
    }
}