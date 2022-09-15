package io.jutil.jdo.internal.core.executor.metadata;

import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.core.parser.TransientMetadata;
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

	private final DataSource dataSource;
	private final io.jutil.jdo.internal.core.parser.MetadataCache configCache;
	private final MetadataCache metadataCache;

	public TableChecker(DataSource ds, io.jutil.jdo.internal.core.parser.MetadataCache cache) {
		this.dataSource = ds;
		this.configCache = cache;
		this.metadataCache = new MetadataCache();
	}

	public void check() {
		new MetadataParser(dataSource, metadataCache);
		List<String> lackList = new ArrayList<>();
		for (var entry : configCache.allEntityMetadata().entrySet()) {
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

	private void checkColumn(TableMetadata table, EntityMetadata config) {
		var fieldMap = config.getFieldMap();
		var columnMap = table.getFieldMap();
		List<String> lackList = new ArrayList<>();

		for (var entry : fieldMap.entrySet()) {
			if (entry.getValue() instanceof TransientMetadata) {
				continue;
			}

			if (!columnMap.containsKey(entry.getValue().getColumnName())) {
				lackList.add(entry.getValue().getColumnName());
			}
		}
		if (!lackList.isEmpty()) {
			logger.warn("表 [{}] 缺少列: {}", table.getTableName(), lackList);
		}
	}
}
