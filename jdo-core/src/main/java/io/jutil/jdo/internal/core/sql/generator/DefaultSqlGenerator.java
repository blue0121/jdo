package io.jutil.jdo.internal.core.sql.generator;

import io.jutil.jdo.internal.core.parser.model.DefaultEntityConfig;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlConfig;
import io.jutil.jdo.internal.core.parser.model.DefaultSqlItem;
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

	private final DefaultEntityConfig entityConfig;
	private final DefaultSqlConfig sqlConfig;

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

	public DefaultSqlGenerator(DefaultEntityConfig entityConfig) {
		this.entityConfig = entityConfig;
		this.sqlConfig = new DefaultSqlConfig();
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
		entityConfig.setSqlConfig(sqlConfig);
	}

	private void generateSelectById() {
		String sql = String.format(SqlConst.SELECT_TPL, table,
				StringUtil.join(whereIdColumnList, SqlConst.AND));
		var item = new DefaultSqlItem(sql, whereIdFieldList);
		sqlConfig.setSelectById(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 select-id-sql: {}", className, item);
		}
	}

	private void generateSelectByIdList() {
		if (entityConfig.getIdMap().size() > 1) {
			return;
		}

		String sql = String.format(SqlConst.SELECT_TPL, table, idColumn + SqlConst.IN_PLACEHOLDER);
		var item = new DefaultSqlItem(sql, List.of(idField));
		sqlConfig.setSelectByIdList(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 select-idList-sql: {}", className, item);
		}
	}

	private void generateInsert() {
		String sql = String.format(SqlConst.INSERT_TPL, table,
				StringUtil.join(insertColumnList, SqlConst.SEPARATOR),
				StringUtil.repeat(SqlConst.PLACEHOLDER, insertFieldList.size(), SqlConst.SEPARATOR));
		var item = new DefaultSqlItem(sql, insertFieldList);
		sqlConfig.setInsert(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 insert-sql: {}", className, item);
		}
	}

	private void generateUpdateById() {
		if (entityConfig.getColumnMap().size() == 0) {
			return;
		}

		List<String> fieldList = new ArrayList<>(updateFieldList);
		fieldList.addAll(whereIdFieldList);
		String sql = String.format(SqlConst.UPDATE_TPL, table,
				StringUtil.join(updateColumnList, SqlConst.SEPARATOR),
				StringUtil.join(whereIdColumnList, SqlConst.AND));
		var item = new DefaultSqlItem(sql, fieldList);
		sqlConfig.setUpdateById(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 update-id-sql: {}", className, item);
		}
	}

	private void generateUpdateByIdAndVersion() {
		var version = entityConfig.getVersionConfig();
		if (version == null) {
			return;
		}

		List<String> fieldList = new ArrayList<>(updateFieldList);
		fieldList.addAll(whereIdFieldList);
		fieldList.add(version.getFieldName());

		List<String> columnList = new ArrayList<>(updateColumnList);
		columnList.add(version.getEscapeColumnName() + "=" + version.getEscapeColumnName() + "+1");
		List<String> whereList = new ArrayList<>(whereIdColumnList);
		whereList.add(version.getEscapeColumnName() + SqlConst.EQUAL_PLACEHOLDER);

		String sql = String.format(SqlConst.UPDATE_TPL, table,
				StringUtil.join(columnList, SqlConst.SEPARATOR),
				StringUtil.join(whereList, SqlConst.AND));
		var item = new DefaultSqlItem(sql, fieldList);
		sqlConfig.setUpdateByIdAndVersion(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 update-id-version-sql: {}", className, item);
		}
	}

	private void generateDeleteById() {
		String sql = String.format(SqlConst.DELETE_TPL, table,
				StringUtil.join(whereIdColumnList, SqlConst.AND));
		var item = new DefaultSqlItem(sql, whereIdFieldList);
		sqlConfig.setDeleteById(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 delete-id-sql: {}", className, item);
		}
	}

	private void generateDeleteByIdList() {
		if (entityConfig.getIdMap().size() > 1) {
			return;
		}

		String sql = String.format(SqlConst.DELETE_TPL, table, idColumn + SqlConst.IN_PLACEHOLDER);
		var item = new DefaultSqlItem(sql, List.of(idField));
		sqlConfig.setDeleteByIdList(item);
		if (logger.isDebugEnabled()) {
			logger.debug("{} 生成 delete-idList-sql: {}", className, item);
		}
	}

	private void init() {
		className = entityConfig.getClazz().getName();
		table = entityConfig.getEscapeTableName();
		var id = entityConfig.getIdConfig();
		if (id != null) {
			idField = id.getFieldName();
			idColumn = id.getEscapeColumnName();
		}

		insertFieldList = new ArrayList<>();
		insertColumnList = new ArrayList<>();

		whereIdFieldList = new ArrayList<>();
		whereIdColumnList = new ArrayList<>();

		updateFieldList = new ArrayList<>();
		updateColumnList = new ArrayList<>();

		for (var entry : entityConfig.getIdMap().entrySet()) {
			var config = entry.getValue();

			if (!config.isDbGenerated()) {
				insertFieldList.add(entry.getKey());
				insertColumnList.add(config.getEscapeColumnName());
			}

			whereIdFieldList.add(entry.getKey());
			whereIdColumnList.add(config.getEscapeColumnName() + SqlConst.EQUAL_PLACEHOLDER);
		}

		if (entityConfig.getVersionConfig() != null) {
			var config = entityConfig.getVersionConfig();
			insertFieldList.add(config.getFieldName());
			insertColumnList.add(config.getEscapeColumnName());
		}

		for (var entry : entityConfig.getColumnMap().entrySet()) {
			var config = entry.getValue();

			insertFieldList.add(entry.getKey());
			insertColumnList.add(config.getEscapeColumnName());

			updateFieldList.add(entry.getKey());
			updateColumnList.add(config.getEscapeColumnName() + SqlConst.EQUAL_PLACEHOLDER);
		}

	}

}
