package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.parser.ColumnConfig;
import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.IdConfig;
import io.jutil.jdo.core.parser.SqlConfig;
import io.jutil.jdo.core.parser.VersionConfig;
import io.jutil.jdo.internal.core.util.AssertUtil;

import java.util.Collections;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class DefaultEntityConfig extends DefaultMapperConfig implements EntityConfig {
	private String tableName;
	private String escapeTableName;
	private IdConfig idConfig;
	private Map<String, IdConfig> idMap;
	private VersionConfig versionConfig;
	private Map<String, ColumnConfig> extraMap;
	private SqlConfig sqlConfig;

	public DefaultEntityConfig() {
	}

	@Override
	public void check() {
		super.check();
		AssertUtil.notEmpty(tableName, "表名");
		AssertUtil.notEmpty(escapeTableName, "转义表名");
		AssertUtil.notEmpty(idMap, "@Id 主键配置");
		AssertUtil.notNull(extraMap, "额外字段配置");
		AssertUtil.notNull(sqlConfig, "SQL配置");

		idMap.forEach((k, v) -> v.check());
		extraMap.forEach((k, v) -> v.check());
		sqlConfig.check();
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public String getEscapeTableName() {
		return escapeTableName;
	}

	@Override
	public IdConfig getIdConfig() {
		return idConfig;
	}

	@Override
	public Map<String, IdConfig> getIdMap() {
		return idMap;
	}

	@Override
	public VersionConfig getVersionConfig() {
		return versionConfig;
	}

	@Override
	public Map<String, ColumnConfig> getExtraMap() {
		return extraMap;
	}

	@Override
	public SqlConfig getSqlConfig() {
		return sqlConfig;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setEscapeTableName(String escapeTableName) {
		this.escapeTableName = escapeTableName;
	}

	public void setIdMap(Map<String, IdConfig> idMap) {
		AssertUtil.notEmpty(idMap, "IdConfigMap");
		if (idMap == null || idMap.isEmpty()) {
			this.idMap = Map.of();
		} else if (idMap.size() == 1) {
			this.idConfig = idMap.entrySet().iterator().next().getValue();
			this.idMap = Collections.unmodifiableMap(idMap);
		} else {
			this.idMap = Collections.unmodifiableMap(idMap);
		}
	}

	public void setVersionConfig(VersionConfig versionConfig) {
		this.versionConfig = versionConfig;
	}

	public void setExtraMap(Map<String, ColumnConfig> extraMap) {
		if (extraMap == null || extraMap.isEmpty()) {
			this.extraMap = Map.of();
		} else {
			this.extraMap = Collections.unmodifiableMap(extraMap);
		}
	}

	public void setSqlConfig(SqlConfig sqlConfig) {
		this.sqlConfig = sqlConfig;
	}
}
