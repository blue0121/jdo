package io.jutil.jdo.internal.core.executor.mapper;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
public class UpdateIdMapHandler implements MapHandler {
	public UpdateIdMapHandler() {
	}

	@Override
	public void handle(MapRequest request, MapResponse response) {
		var idMap = request.getConfig().getIdMap();
		for (var entry : idMap.entrySet()) {
			var id = entry.getValue();
			var beanField = id.getBeanField();
			var value = beanField.getFieldValue(request.getTarget());
			response.putField(id.getFieldName(), value);
		}
	}
}
