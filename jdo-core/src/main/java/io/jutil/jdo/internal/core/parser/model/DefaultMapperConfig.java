package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.parser.ColumnConfig;
import io.jutil.jdo.core.parser.ConfigType;
import io.jutil.jdo.core.parser.MapperConfig;
import io.jutil.jdo.core.reflect.JavaBean;
import io.jutil.jdo.internal.core.util.AssertUtil;

import java.util.Collections;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class DefaultMapperConfig implements MapperConfig {
	protected ConfigType configType;
	protected Class<?> clazz;
	protected JavaBean javaBean;
	protected Map<String, ColumnConfig> columnMap;

	public DefaultMapperConfig() {
		this.configType = ConfigType.MAPPER;
	}

	@Override
	public ConfigType getConfigType() {
		return configType;
	}

	@Override
	public void check() {
		AssertUtil.notNull(clazz, "Class");
		AssertUtil.notNull(javaBean, "JavaBean");
		AssertUtil.notNull(columnMap, "ColumnConfigMap");
		for (var entry : columnMap.entrySet()) {
			entry.getValue().check();
		}
	}

	@Override
	public Class<?> getClazz() {
		return clazz;
	}

	@Override
	public JavaBean getJavaBean() {
		return javaBean;
	}

	@Override
	public Map<String, ColumnConfig> getColumnMap() {
		return columnMap;
	}

	public void setJavaBean(JavaBean javaBean) {
		AssertUtil.notNull(javaBean, "JavaBean");
		this.clazz = javaBean.getTargetClass();
		this.javaBean = javaBean;
	}

	public void setColumnMap(Map<String, ColumnConfig> columnMap) {
		if (columnMap == null || columnMap.isEmpty()) {
			this.columnMap = Map.of();
		} else {
			this.columnMap = Collections.unmodifiableMap(columnMap);
		}
	}
}
