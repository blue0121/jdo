package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.exception.EntityFieldException;
import io.jutil.jdo.core.parser.ColumnConfig;
import io.jutil.jdo.core.parser.FieldConfig;
import io.jutil.jdo.core.parser.IdConfig;
import io.jutil.jdo.core.parser.VersionConfig;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-21
 */
@NoArgsConstructor
public abstract class AbstractSqlHandler implements SqlHandler {


	public ColumnConfig getColumn(String name, Map<String, ColumnConfig> columnMap) {
		var column = columnMap.get(name);
		if (column == null) {
			throw new EntityFieldException(name);
		}
		return column;
	}

	public String getColumnString(String name, Map<String, IdConfig> idMap, Map<String, ColumnConfig> columnMap,
	                                 VersionConfig version) {
		if (columnMap != null && columnMap.get(name) != null) {
			return columnMap.get(name).getEscapeColumnName();
		}
		if (idMap != null && idMap.get(name) != null) {
			return idMap.get(name).getEscapeColumnName();
		}
		if (version != null && version.getFieldName().equals(name)) {
			return version.getEscapeColumnName();
		}
		throw new EntityFieldException(name);
	}

	protected void putParam(SqlRequest request, SqlResponse response, FieldConfig config) {
		var field = config.getFieldName();
		var beanField = config.getBeanField();
		var value = beanField.getFieldValue(request.getTarget());
		if (request.isDynamic() && this.isEmpty(value)) {
			response.removeParam(field);
		} else {
			response.putParam(field, value);
		}
	}

	protected boolean isEmpty(Object value) {
		return value == null || "".equals(value);
	}

}
