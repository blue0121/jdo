package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.parser.FieldMetadata;
import io.jutil.jdo.core.reflect.ClassFieldOperation;
import io.jutil.jdo.internal.core.util.AssertUtil;

/**
 * @author Jin Zheng
 * @since 2022-05-11
 */
public abstract class DefaultFieldMetadata implements FieldMetadata {
    protected String fieldName;
    protected String columnName;
    protected ClassFieldOperation fieldOperation;

	protected DefaultFieldMetadata() {
	}

    public void check() {
        AssertUtil.notEmpty(fieldName, "字段名");
        AssertUtil.notEmpty(columnName, "列名");
        AssertUtil.notNull(fieldOperation, "字段操作");
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
    public ClassFieldOperation getFieldOperation() {
        return fieldOperation;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setBeanField(ClassFieldOperation fieldOperation) {
        AssertUtil.notNull(fieldOperation, "字段");
        this.fieldName = fieldOperation.getFieldName();
        this.fieldOperation = fieldOperation;
    }
}
