package io.jutil.jdo.internal.core.executor.metadata;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
@NoArgsConstructor
public class MetadataCache {
    private final Map<String, TableMetadata> tableMap = new HashMap<>();


	public Set<String> getTableNames() {
		return tableMap.keySet();
	}

	public TableMetadata getTable(String tableName) {
		return tableMap.get(tableName);
	}

	public void putTable(TableMetadata table) {
		tableMap.put(table.getTableName().toLowerCase(), table);
		tableMap.put(table.getTableName().toUpperCase(), table);
	}

}
