package io.jutil.jdo.internal.core.sql;

import io.jutil.jdo.core.parser.FieldMetadata;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SqlParameter that = (SqlParameter) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
