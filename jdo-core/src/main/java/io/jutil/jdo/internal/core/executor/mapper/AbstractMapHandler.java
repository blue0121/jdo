package io.jutil.jdo.internal.core.executor.mapper;

import io.jutil.jdo.core.parser.FieldConfig;

/**
 * @author Jin Zheng
 * @since 2022-03-18
 */
public abstract class AbstractMapHandler implements MapHandler {
	public AbstractMapHandler() {
	}

	protected void putObject(MapRequest request, MapResponse response, FieldConfig config) {
		var field = config.getFieldName();
		var map = request.getMap();
		if (map == null) {
			var beanField = config.getBeanField();
			var value = beanField.getFieldValue(request.getTarget());
			if (request.isDynamic() && this.isEmpty(value)) {
				response.removeField(field);
			} else {
				response.putField(field, value);
			}
		} else {
			var value = map.get(field);
			response.putField(field, value);
		}
	}

	private boolean isEmpty(Object value) {
		return value == null || "".equals(value);
	}

}
