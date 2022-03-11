package io.jutil.jdo.internal.core.executor.mapper;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.FieldConfig;
import io.jutil.jdo.core.parser.MapperConfig;
import io.jutil.jdo.core.reflect.BeanField;
import io.jutil.jdo.core.reflect.JavaBean;
import io.jutil.jdo.internal.core.util.EnumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengj
 * @since 2022-02-21
 */
public class ObjectRowMapper<T> implements RowMapper<T> {
	private static Logger logger = LoggerFactory.getLogger(ObjectRowMapper.class);

	private final JavaBean javaBean;
	private final Map<String, BeanField> fieldMap = new HashMap<>();

	public ObjectRowMapper(EntityConfig config) {
		this.javaBean = config.getJavaBean();
		config.getIdMap().forEach((k, v) -> this.setFieldMap(v));
		this.setFieldMap(config.getVersionConfig());
		config.getColumnMap().forEach((k, v) -> this.setFieldMap(v));
		config.getExtraMap().forEach((k, v) -> this.setFieldMap(v));
	}

	public ObjectRowMapper(MapperConfig config) {
		this.javaBean = config.getJavaBean();
		config.getColumnMap().forEach((k, v) -> this.setFieldMap(v));
		if (config instanceof EntityConfig c) {
			c.getIdMap().forEach((k, v) -> this.setFieldMap(v));
			this.setFieldMap(c.getVersionConfig());
			c.getExtraMap().forEach((k, v) -> this.setFieldMap(v));
		}
	}

	private void setFieldMap(FieldConfig field) {
		if (field == null) {
			return;
		}

		fieldMap.put(field.getColumnName().toLowerCase(), field.getBeanField());
		fieldMap.put(field.getColumnName().toUpperCase(), field.getBeanField());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getType() {
		return (Class<T>) javaBean.getTargetClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		T object = (T) javaBean.newInstanceQuietly();
		if (object == null) {
			throw new JdbcException("Unable to instance: " + javaBean.getName());
		}

		for (int i = ONE; i <= meta.getColumnCount(); i++) {
			String label = meta.getColumnLabel(i);
			BeanField field = fieldMap.get(label);
			if (field != null) {
				Class<?> type = field.getField().getType();
				Object value = this.convert(rs, i, type);
				field.setFieldValue(object, value);
			}
		}

		return object;
	}

	private Object convert(ResultSet rs, int i, Class<?> type) throws SQLException {
		if (type == byte.class) {
			return rs.getByte(i);
		} else if (type == short.class) {
			return rs.getShort(i);
		} else if (type == int.class) {
			return rs.getInt(i);
		} else if (type == long.class) {
			return rs.getLong(i);
		} else if (type == float.class) {
			return rs.getFloat(i);
		} else if (type == double.class) {
			return rs.getDouble(i);
		} else if (type == Byte.class) {
			byte v = rs.getByte(i);
			return rs.wasNull() ? null : v;
		} else if (type == Short.class) {
			short v = rs.getShort(i);
			return rs.wasNull() ? null : v;
		} else if (type == Integer.class) {
			int v = rs.getInt(i);
			return rs.wasNull() ? null : v;
		} else if (type == Long.class) {
			long v = rs.getLong(i);
			return rs.wasNull() ? null : v;
		} else if (type == Float.class) {
			float v = rs.getFloat(i);
			return rs.wasNull() ? null : v;
		} else if (type == Double.class) {
			double v = rs.getDouble(i);
			return rs.wasNull() ? null : v;
		} else if (type == BigDecimal.class) {
			return rs.getBigDecimal(i);
		} else if (type == String.class) {
			return rs.getString(i);
		} else if (type == java.sql.Date.class) {
			return rs.getDate(i);
		} else if (type == Time.class) {
			return rs.getTime(i);
		} else if (type == Timestamp.class || type == Date.class) {
			return rs.getTimestamp(i);
		} else if (type == Instant.class) {
			Timestamp timestamp = rs.getTimestamp(i);
			return timestamp == null ? null : timestamp.toInstant();
		} else if (type == LocalDate.class) {
			java.sql.Date date = rs.getDate(i);
			return date == null ? null : date.toLocalDate();
		} else if (type == LocalTime.class) {
			Time time = rs.getTime(i);
			return time == null ? null : time.toString();
		} else if (type == LocalDateTime.class) {
			Timestamp timestamp = rs.getTimestamp(i);
			return timestamp == null ? null : timestamp.toLocalDateTime();
		} else if (type.isEnum()) {
			String val = rs.getString(i);
			if (val == null || val.isEmpty()) {
				return null;
			}
			return EnumUtil.fromString(type, val);
		}

		return rs.getObject(i);
	}


}
