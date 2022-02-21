package io.jutil.jdo.internal.core.executor.metadata;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
@Getter
@Setter
@NoArgsConstructor
public class TableMetadata {
	private String tableName;
	private String comment;
	private Map<String, IdMetadata> idMap = new HashMap<>();
	private Map<String, ColumnMetadata> columnMap = new HashMap<>();

	public void setIdMap(IdMetadata id) {
		idMap.put(id.getColumnName(), id);
	}

	public void setColumnMap(ColumnMetadata column) {
		columnMap.put(column.getColumnName(), column);
	}

}
