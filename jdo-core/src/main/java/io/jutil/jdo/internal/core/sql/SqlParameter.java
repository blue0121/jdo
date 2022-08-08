package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser.FieldMetadata;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Jin Zheng
 * @since 2022-06-28
 */
@Getter
@NoArgsConstructor
public class SqlParameter {
    private FieldMetadata metadata;
    private Object value;

	public static SqlParameter create(Object value) {
		return create(null, value);
	}

	public static SqlParameter create(FieldMetadata metadata, Object value) {
		var parameter = new SqlParameter();
		parameter.metadata = metadata;
		parameter.value = value;
		return parameter;
	}

	@Override
	public String toString() {
		return value == null ? null : value.toString();
	}
}
