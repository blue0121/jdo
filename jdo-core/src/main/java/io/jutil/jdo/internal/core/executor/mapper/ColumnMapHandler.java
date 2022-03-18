package io.jutil.jdo.internal.core.executor.mapper;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
public class ColumnMapHandler implements MapHandler {
	public ColumnMapHandler() {
	}

	@Override
	public void handle(MapRequest request, MapResponse response) {
		var columnMap = request.getConfig().getColumnMap();
		for (var entry : columnMap.entrySet()) {
			var column = entry.getValue();
			var beanField = column.getBeanField();
			var value = beanField.getFieldValue(request.getTarget());
			if (this.isEmpty(value)) {
				response.removeField(column.getFieldName());
			} else {
				response.putField(column.getFieldName(), value);
			}
		}
	}

	private boolean isEmpty(Object value) {
		return value == null || "".equals(value);
	}
}
