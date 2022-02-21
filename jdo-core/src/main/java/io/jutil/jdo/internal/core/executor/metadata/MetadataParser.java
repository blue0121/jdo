package io.jutil.jdo.internal.core.executor.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class MetadataParser {
	private static Logger logger = LoggerFactory.getLogger(MetadataParser.class);

	private final MetadataCache cache;

	public MetadataParser(DataSource ds, MetadataCache cache) {
		this.cache = cache;
		try (var conn = ds.getConnection()) {
			this.init(conn);
		} catch (SQLException e) {
			logger.error("数据库连接错误, ", e);
		}
	}

	private void init(Connection conn) throws SQLException {
		var meta = conn.getMetaData();
		List<TableMetadata> tableList = this.parseTable(meta);

		for (var table : tableList) {
			this.parseColumn(meta, table);
			cache.putTable(table);
		}
	}

	private List<TableMetadata> parseTable(DatabaseMetaData meta) throws SQLException {
		List<TableMetadata> tableList = new ArrayList<>();
		try (var rs = meta.getTables(null, null, null, new String[]{"TABLE"})) {
			while (rs.next()) {
				var name = rs.getString("TABLE_NAME");
				var comment = rs.getString("REMARKS");
				var table = new TableMetadata();
				table.setTableName(name);
				table.setComment(comment);
				tableList.add(table);
			}
		}
		logger.info("所有表: {}", tableList);
		return tableList;
	}

	private void parseColumn(DatabaseMetaData meta, TableMetadata table) throws SQLException {
		try (var rs = meta.getPrimaryKeys(null, null, table.getTableName())) {
			while (rs.next()) {
				var name = rs.getString("COLUMN_NAME");

				var id = new IdMetadata();
				id.setColumnName(name);
				table.setIdMap(id);
			}
		}
		try (var rs = meta.getColumns(null, null, table.getTableName(), null)) {
			while (rs.next()) {
				var name = rs.getString("COLUMN_NAME");
				int type = rs.getInt("DATA_TYPE");
				var typeName = rs.getString("TYPE_NAME");
				int size = rs.getInt("COLUMN_SIZE");
				var nullable = rs.getString("IS_NULLABLE");
				var inc = rs.getString("IS_AUTOINCREMENT");
				var def = rs.getString("COLUMN_DEF");
				var comment = rs.getString("REMARKS");

				var id = table.getIdMap().get(name);
				if (id != null) {
					id.setInc(!"NO".equals(inc));
				}
				ColumnMetadata column = id;
				if (column == null) {
					column = new ColumnMetadata();
					column.setColumnName(name);
					table.setColumnMap(column);
				}
				column.setType(type);
				column.setTypeName(typeName);
				column.setSize(size);
				column.setNullable(!"NO".equals(nullable));
				column.setDef("NULL".equals(def) ? null : def);
				column.setComment(comment);
			}
		}
	}

}
