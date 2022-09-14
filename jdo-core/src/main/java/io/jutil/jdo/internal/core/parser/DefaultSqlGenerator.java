package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.internal.core.parser.model.DefaultEntityMetadata;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlItem;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlMetadata;
import io.jutil.jdo.internal.core.sql.SqlConst;
import io.jutil.jdo.internal.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class DefaultSqlGenerator {
	private static Logger logger = LoggerFactory.getLogger(DefaultSqlGenerator.class);

	private final DefaultEntityMetadata entityMetadata;
	private final DefaultSqlMetadata sqlMetadata;

	private String className;
	private String table;
	private String idField;
	private String idColumn;

	private List<String> insertFieldList;
	private List<String> insertColumnList;

	private List<String> whereIdFieldList;
	private List<String> whereIdColumnList;

	private List<String> updateColumnList;
	private List<String> updateFieldList;

	public DefaultSqlGenerator(DefaultEntityMetadata entityMetadata) {
		this.entityMetadata = entityMetadata;
		this.sqlMetadata = new DefaultSqlMetadata();
		this.init();
	}

	public void generate() {
		this.generateSelectById();
		this.generateSelectByIdList();
		this.generateInsert();
		this.generateUpdateById();
		this.generateUpdateByIdAndVersion();
		this.generateDeleteById();
		this.generateDeleteByIdList();
		entityMetadata.setSqlMetadata(sqlMetadata);
	}

	private void generateSelectById() {
		String sql = String.format(SqlConst.SELECT_TPL, table,
				StringUtil.join(whereIdColumnList, SqlConst.AND));
		var item = new DefaultSqlItem(sql, whereIdFieldList);
		sqlMetadata.setSelectById(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 select-id-sql: {}", className, item);
		}
	}

	private void generateSelectByIdList() {
		if (entityMetadata.getIdMap().size() > 1) {
			return;
		}

		String sql = String.format(SqlConst.SELECT_TPL, table, idColumn + SqlConst.IN_PLACEHOLDER);
		var item = new DefaultSqlItem(sql, List.of(idField));
		sqlMetadata.setSelectByIdList(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 select-idList-sql: {}", className, item);
		}
	}

	private void generateInsert() {
		String sql = String.format(SqlConst.INSERT_TPL, table,
				StringUtil.join(insertColumnList, SqlConst.SEPARATOR),
				StringUtil.repeat(SqlConst.PLACEHOLDER, insertFieldList.size(), SqlConst.SEPARATOR));
		var item = new DefaultSqlItem(sql, insertFieldList);
		sqlMetadata.setInsert(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 insert-sql: {}", className, item);
		}
	}

	private void generateUpdateById() {
		if (entityMetadata.getColumnMap().size() == 0) {
			return;
		}

		List<String> fieldList = new ArrayList<>(updateFieldList);
		fieldList.addAll(whereIdFieldList);
		String sql = String.format(SqlConst.UPDATE_TPL, table,
				StringUtil.join(updateColumnList, SqlConst.SEPARATOR),
				StringUtil.join(whereIdColumnList, SqlConst.AND));
		var item = new DefaultSqlItem(sql, fieldList);
		sqlMetadata.setUpdateById(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 update-id-sql: {}", className, item);
		}
	}

	private void generateUpdateByIdAndVersion() {
		var version = entityMetadata.getVersionMetadata();
		if (version == null) {
			return;
		}

		List<String> fieldList = new ArrayList<>(updateFieldList);
		fieldList.addAll(whereIdFieldList);
		fieldList.add(version.getFieldName());

		List<String> columnList = new ArrayList<>(updateColumnList);
		columnList.add(version.getColumnName() + "=" + version.getColumnName() + "+1");
		List<String> whereList = new ArrayList<>(whereIdColumnList);
		whereList.add(version.getColumnName() + SqlConst.EQUAL_PLACEHOLDER);

		String sql = String.format(SqlConst.UPDATE_TPL, table,
				StringUtil.join(columnList, SqlConst.SEPARATOR),
				StringUtil.join(whereList, SqlConst.AND));
		var item = new DefaultSqlItem(sql, fieldList);
		sqlMetadata.setUpdateByIdAndVersion(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 update-id-version-sql: {}", className, item);
		}
	}

	private void generateDeleteById() {
		String sql = String.format(SqlConst.DELETE_TPL, table,
				StringUtil.join(whereIdColumnList, SqlConst.AND));
		var item = new DefaultSqlItem(sql, whereIdFieldList);
		sqlMetadata.setDeleteById(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 delete-id-sql: {}", className, item);
		}
	}

	private void generateDeleteByIdList() {
		if (entityMetadata.getIdMap().size() > 1) {
			return;
		}

		String sql = String.format(SqlConst.DELETE_TPL, table, idColumn + SqlConst.IN_PLACEHOLDER);
		var item = new DefaultSqlItem(sql, List.of(idField));
		sqlMetadata.setDeleteByIdList(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 delete-idList-sql: {}", className, item);
		}
	}

	private void init() {
		className = entityMetadata.getTargetClass().getName();
		table = entityMetadata.getTableName();
		var id = entityMetadata.getIdMetadata();
		if (id != null) {
			idField = id.getFieldName();
			idColumn = id.getColumnName();
		}

		insertFieldList = new ArrayList<>();
		insertColumnList = new ArrayList<>();

		whereIdFieldList = new ArrayList<>();
		whereIdColumnList = new ArrayList<>();

		updateFieldList = new ArrayList<>();
		updateColumnList = new ArrayList<>();

		for (var entry : entityMetadata.getIdMap().entrySet()) {
			var config = entry.getValue();

			if (!config.isDbGenerated()) {
				insertFieldList.add(entry.getKey());
				insertColumnList.add(config.getColumnName());
			}

			whereIdFieldList.add(entry.getKey());
			whereIdColumnList.add(config.getColumnName() + SqlConst.EQUAL_PLACEHOLDER);
		}

		if (entityMetadata.getVersionMetadata() != null) {
			var config = entityMetadata.getVersionMetadata();
			insertFieldList.add(config.getFieldName());
			insertColumnList.add(config.getColumnName());
		}

		for (var entry : entityMetadata.getColumnMap().entrySet()) {
			var config = entry.getValue();

			insertFieldList.add(entry.getKey());
			insertColumnList.add(config.getColumnName());

			updateFieldList.add(entry.getKey());
			updateColumnList.add(config.getColumnName() + SqlConst.EQUAL_PLACEHOLDER);
		}

	}

}
