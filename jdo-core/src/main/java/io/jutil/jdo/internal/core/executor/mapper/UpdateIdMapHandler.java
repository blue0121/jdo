package io.jutil.jdo.internal.core.executor.mapper;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
public class UpdateIdMapHandler extends AbstractMapHandler {
	public UpdateIdMapHandler() {
	}

	@Override
	public void handle(MapRequest request, MapResponse response) {
		var idMap = request.getConfig().getIdMap();
		for (var entry : idMap.entrySet()) {
			this.putObject(request, response, entry.getValue());
		}
	}
}
