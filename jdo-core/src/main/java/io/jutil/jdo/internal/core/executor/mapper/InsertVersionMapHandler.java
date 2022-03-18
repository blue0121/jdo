package io.jutil.jdo.internal.core.executor.mapper;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
public class InsertVersionMapHandler implements MapHandler {
	public InsertVersionMapHandler() {
	}

	@Override
	public void handle(MapRequest request, MapResponse response) {
		var version = request.getConfig().getVersionConfig();
		if (version == null) {
			return;
		}

		var map = request.getMap();
		Object value = null;
		if (map == null) {
			var beanField = version.getBeanField();
			value = beanField.getFieldValue(request.getTarget());
			if (value == null) {
				value = version.getDefaultValue();
				beanField.setFieldValue(request.getTarget(), value);
			}
		} else {
			var field = version.getFieldName();
			value = map.get(field);
			if (value == null) {
				value = version.getDefaultValue();
			}
		}
		response.putField(version.getFieldName(), value);
	}
}
