package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.parser.ColumnMetadata;
import io.jutil.jdo.core.parser.FieldMetadata;
import io.jutil.jdo.core.parser.MapperMetadata;
import io.jutil.jdo.core.parser.MetadataType;
import io.jutil.jdo.core.reflect.ClassOperation;
import io.jutil.jdo.internal.core.util.AssertUtil;

import java.util.Collections;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-05-12
 */
public class DefaultMapperMetadata implements MapperMetadata {
	protected MetadataType metadataType;
	protected Class<?> targetClass;
	protected ClassOperation classOperation;
	protected Map<String, ColumnMetadata> columnMap;
	protected Map<String, FieldMetadata> fieldMap;

	public DefaultMapperMetadata() {
		this.metadataType = MetadataType.MAPPER;
	}

	public void check() {
		AssertUtil.notNull(targetClass, "目标类型");
		AssertUtil.notNull(classOperation, "类操作");
		AssertUtil.notNull(columnMap, "普通字段配置");

		columnMap.forEach((k, v) -> this.check(v));
	}

	protected final void check(FieldMetadata field) {
		if (field instanceof DefaultFieldMetadata f) {
			f.check();
		}
	}

	@Override
	public MetadataType getMetadataType() {
		return metadataType;
	}

	@Override
	public Class<?> getTargetClass() {
		return targetClass;
	}

	@Override
	public ClassOperation getClassOperation() {
		return classOperation;
	}

	@Override
	public Map<String, ColumnMetadata> getColumnMap() {
		return columnMap;
	}

	@Override
	public Map<String, FieldMetadata> getFieldMap() {
		return fieldMap;
	}

	public void setClassOperation(ClassOperation classOperation) {
		AssertUtil.notNull(classOperation, "JavaBean");
		this.targetClass = classOperation.getTargetClass();
		this.classOperation = classOperation;
	}

	public void setColumnMap(Map<String, ColumnMetadata> columnMap) {
		if (columnMap == null || columnMap.isEmpty()) {
			this.columnMap = Map.of();
		} else {
			this.columnMap = Collections.unmodifiableMap(columnMap);
		}
	}

	public void setFieldMap(Map<String, FieldMetadata> fieldMap) {
		if (fieldMap == null || fieldMap.isEmpty()) {
			this.fieldMap = Map.of();
		} else {
			this.fieldMap = Collections.unmodifiableMap(fieldMap);
		}
	}
}
