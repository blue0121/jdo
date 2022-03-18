package io.jutil.jdo.internal.core.executor.mapper;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
public class ColumnMapHandler extends AbstractMapHandler {
	public ColumnMapHandler() {
	}

	@Override
	public void handle(MapRequest request, MapResponse response) {
		var columnMap = request.getConfig().getColumnMap();
		var map = request.getMap();
		if (map == null) {
			for (var entry : columnMap.entrySet()) {
				this.putObject(request, response, entry.getValue());
			}
		} else {
			for (var entry : map.entrySet()) {
				response.putField(entry.getKey(), entry.getValue());
			}
		}

	}

}
