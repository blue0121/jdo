package io.jutil.jdo.internal.core.executor.metadata;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.internal.core.parser.EntityConfigCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class TableChecker {
	private static Logger logger = LoggerFactory.getLogger(TableChecker.class);

	private final EntityConfigCache entityConfigCache;
	private final MetadataCache metadataCache;
	private final MetadataParser metadataParser;

	public TableChecker(DataSource ds, EntityConfigCache cache) {
		this.entityConfigCache = cache;
		this.metadataCache = new MetadataCache();
		this.metadataParser = new MetadataParser(ds, metadataCache);
	}

	public void check() {
		List<String> lackList = new ArrayList<>();
		for (var entry : entityConfigCache.all().entrySet()) {
			var config = entry.getValue();
			var table = metadataCache.getTable(config.getTableName());
			if (table == null) {
				lackList.add(config.getTableName());
				continue;
			}
			this.checkColumn(table, config);
		}
		if (!lackList.isEmpty()) {
			logger.warn("缺少表: {}", lackList);
		}
	}

	private void checkColumn(TableMetadata table, EntityConfig config) {
		var idMap = table.getIdMap();
		var columnMap = table.getColumnMap();
		List<String> lackList = new ArrayList<>();

		var version = config.getVersionConfig();
		if (version != null && !columnMap.containsKey(version.getColumnName())) {
			lackList.add(version.getColumnName());
		}
		for (var entry : config.getIdMap().entrySet()) {
			if (!idMap.containsKey(entry.getValue().getColumnName())) {
				lackList.add(entry.getValue().getColumnName());
			}
		}
		for (var entry : config.getColumnMap().entrySet()) {
			if (!columnMap.containsKey(entry.getValue().getColumnName())) {
				lackList.add(entry.getValue().getColumnName());
			}
		}
		if (!lackList.isEmpty()) {
			logger.warn("表 [{}] 缺少列: {}", table.getTableName(), lackList);
		}
	}
}