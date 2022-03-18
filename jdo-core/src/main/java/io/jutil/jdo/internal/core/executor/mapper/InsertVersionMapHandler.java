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
		var beanField = version.getBeanField();
		var value = beanField.getFieldValue(request.getTarget());
		if (value == null) {
			value = version.getDefaultValue();
			beanField.setFieldValue(request.getTarget(), value);
		}
		response.putField(version.getFieldName(), value);
	}
}
