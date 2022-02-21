package io.jutil.jdo.internal.core.executor.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class MetadataCache {
    private final Map<String, TableMetadata> tableMap = new HashMap<>();

	public MetadataCache() {
	}

	public Set<String> getTableNames() {
		return tableMap.keySet();
	}

	public TableMetadata getTable(String tableName) {
		return tableMap.get(tableName);
	}

	public void putTable(TableMetadata table) {
		tableMap.put(table.getTableName(), table);
	}

}
