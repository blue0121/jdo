package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.parser.FieldConfig;
import io.jutil.jdo.core.reflect.BeanField;
import io.jutil.jdo.internal.core.util.AssertUtil;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class DefaultFieldConfig implements FieldConfig {
	protected String fieldName;
	protected String columnName;
	protected String escapeColumnName;
	protected BeanField beanField;

	public DefaultFieldConfig() {
	}

	@Override
	public void check() {
		AssertUtil.notEmpty(fieldName, "字段名");
		AssertUtil.notEmpty(columnName, "列名");
		AssertUtil.notEmpty(escapeColumnName, "转义列名");
		AssertUtil.notNull(beanField, "字段");
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public String getEscapeColumnName() {
		return escapeColumnName;
	}

	@Override
	public BeanField getBeanField() {
		return beanField;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setEscapeColumnName(String escapeColumnName) {
		this.escapeColumnName = escapeColumnName;
	}

	public void setBeanField(BeanField beanField) {
		AssertUtil.notNull(beanField, "字段");
		this.fieldName = beanField.getField().getName();
		this.beanField = beanField;
	}
}
