package io.jutil.jdo.internal.core.executor.mapper;

import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.internal.core.id.IdGenerator;
import io.jutil.jdo.internal.core.id.SnowflakeId;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
public class InsertIdMapHandler extends AbstractMapHandler {
	private final SnowflakeId snowflakeId;

	public InsertIdMapHandler(SnowflakeId snowflakeId) {
		this.snowflakeId = snowflakeId;
	}

	@Override
	public void handle(MapRequest request, MapResponse response) {
		var idMap = request.getConfig().getIdMap();
		for (var entry : idMap.entrySet()) {
			var id = entry.getValue();
			var field = id.getFieldName();
			switch (id.getGeneratorType()) {
				case AUTO -> this.handleAuto(response, field, id.getIdType());
				case UUID -> this.handleUuid(response, field, id.getIdType());
				case INCREMENT -> this.handleIncrement(response, field, id.getIdType());
				case ASSIGNED -> this.putObject(request, response, id);
				default -> throw new UnsupportedOperationException("不支持主键产生类型: " + id.getGeneratorType());
			}
		}
	}

	private void handleAuto(MapResponse response, String field, IdType idType) {
		switch (idType) {
			case STRING -> response.putField(field, IdGenerator.uuid32bit());
			default -> response.removeField(field);
		}
	}

	private void handleUuid(MapResponse response, String field, IdType idType) {
		switch (idType) {
			case STRING -> response.putField(field, IdGenerator.uuid32bit());
			case LONG -> response.putField(field, snowflakeId.nextId());
			default -> throw new UnsupportedOperationException("不支持主键类型: " + idType);
		}
	}

	private void handleIncrement(MapResponse response, String field, IdType idType) {
		switch (idType) {
			case INT, LONG -> response.removeField(field);
			default -> throw new UnsupportedOperationException("不支持主键类型: " + idType);
		}
	}

}
