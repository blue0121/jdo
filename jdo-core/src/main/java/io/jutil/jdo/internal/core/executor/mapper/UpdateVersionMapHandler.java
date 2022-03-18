package io.jutil.jdo.internal.core.executor.mapper;

import io.jutil.jdo.core.exception.VersionException;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
public class UpdateVersionMapHandler implements MapHandler {
	public UpdateVersionMapHandler() {
	}

	@Override
	public void handle(MapRequest request, MapResponse response) {
		var version = request.getConfig().getVersionConfig();
		if (version == null) {
			return;
		}
		var beanField = version.getBeanField();
		var value = beanField.getFieldValue(request.getTarget());
		if (value != null) {
			response.putField(version.getFieldName(), value);
			return;
		}
		if (version.isForce()) {
			throw new VersionException(request.getClazz(), "缺少版本号");
		}

	}
}
