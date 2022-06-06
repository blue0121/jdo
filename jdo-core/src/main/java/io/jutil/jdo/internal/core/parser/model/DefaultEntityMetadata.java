package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.core.parser.IdMetadata;
import io.jutil.jdo.core.parser.MetadataType;
import io.jutil.jdo.core.parser.SqlMetadata;
import io.jutil.jdo.core.parser.TransientMetadata;
import io.jutil.jdo.core.parser.VersionMetadata;
import io.jutil.jdo.internal.core.util.AssertUtil;

import java.util.Collections;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-05-12
 */
public class DefaultEntityMetadata extends DefaultMapperMetadata implements EntityMetadata {
    private String tableName;
    private String escapeTableName;
    private IdMetadata idMetadata;
    private Map<String, IdMetadata> idMap;
    private VersionMetadata versionMetadata;
    private Map<String, TransientMetadata> transientMap;
    private SqlMetadata sqlMetadata;

	public DefaultEntityMetadata() {
        this.metadataType = MetadataType.ENTITY;
	}

    @Override
    public void check() {
        super.check();
        AssertUtil.notEmpty(tableName, "表名");
        AssertUtil.notEmpty(escapeTableName, "转义表名");
        AssertUtil.notEmpty(idMap, "主键配置");
        AssertUtil.notNull(transientMap, "额外字段配置");
        AssertUtil.notNull(sqlMetadata, "SQL配置");

        idMap.forEach((k, v) -> this.check(v));
        transientMap.forEach((k, v) -> this.check(v));
        if (sqlMetadata instanceof DefaultSqlMetadata s) {
            s.check();
        }
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
    public IdMetadata getIdMetadata() {
        return idMetadata;
    }

    @Override
    public Map<String, IdMetadata> getIdMap() {
        return idMap;
    }

    @Override
    public VersionMetadata getVersionMetadata() {
        return versionMetadata;
    }

    @Override
    public Map<String, TransientMetadata> getTransientMap() {
        return transientMap;
    }

    @Override
    public SqlMetadata getSqlMetadata() {
        return sqlMetadata;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setEscapeTableName(String escapeTableName) {
        this.escapeTableName = escapeTableName;
    }

    public void setIdMap(Map<String, IdMetadata> idMap) {
        AssertUtil.notEmpty(idMap, "IdMetadataMap");
        if (idMap == null || idMap.isEmpty()) {
            this.idMap = Map.of();
        } else if (idMap.size() == 1) {
            this.idMetadata = idMap.entrySet().iterator().next().getValue();
            this.idMap = Collections.unmodifiableMap(idMap);
        } else {
            this.idMap = Collections.unmodifiableMap(idMap);
        }
    }

    public void setVersionMetadata(VersionMetadata versionMetadata) {
        if (this.versionMetadata != null) {
            throw new JdbcException("只能有1个 @Version: " + classOperation.getTargetClass().getName());
        }
        this.versionMetadata = versionMetadata;
    }

    public void setTransientMap(Map<String, TransientMetadata> transientMap) {
        if (transientMap == null || transientMap.isEmpty()) {
            this.transientMap = Map.of();
        } else {
            this.transientMap = Collections.unmodifiableMap(transientMap);
        }
    }

    public void setSqlMetadata(SqlMetadata sqlMetadata) {
        this.sqlMetadata = sqlMetadata;
    }
}
