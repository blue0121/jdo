package io.jutil.jdo.internal.core.executor.mapper;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.internal.core.id.SnowflakeId;
import io.jutil.jdo.internal.core.id.SnowflakeIdFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
public class MapHandlerFacade {
	private static Logger logger = LoggerFactory.getLogger(MapHandlerFacade.class);

	private final SnowflakeId snowflakeId = SnowflakeIdFactory.getSingleSnowflakeId();
	private final List<MapHandler> insertMapHandler = new ArrayList<>();
	private final List<MapHandler> updateMapHandler = new ArrayList<>();

	public MapHandlerFacade() {
		this.init();
	}

	private void init() {
		var insertId = new InsertIdMapHandler(snowflakeId);
		var updateId = new UpdateIdMapHandler();
		var column = new ColumnMapHandler();
		var insertVersion = new InsertVersionMapHandler();
		var updateVersion = new UpdateVersionMapHandler();

		insertMapHandler.add(insertId);
		insertMapHandler.add(column);
		insertMapHandler.add(insertVersion);

		updateMapHandler.add(updateId);
		updateMapHandler.add(column);
		updateMapHandler.add(updateVersion);
	}

	public Map<String, Object> handleInsert(Object target, EntityConfig config, boolean dynamic) {
		return this.handle(target, config, dynamic, insertMapHandler);
	}

	public Map<String, Object> handleUpdate(Object target, EntityConfig config, boolean dynamic) {
		return this.handle(target, config, dynamic, updateMapHandler);
	}

	private Map<String, Object> handle(Object target, EntityConfig config, boolean dynamic, List<MapHandler> handlers) {
		MapRequest request = MapRequest.create(target, config, dynamic);
		MapResponse response = new MapResponse();
		for (var handler : handlers) {
			handler.handle(request, response);
		}
		return response.toFieldMap();
	}

}
