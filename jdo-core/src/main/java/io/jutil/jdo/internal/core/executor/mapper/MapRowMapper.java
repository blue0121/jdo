package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class MapRowMapper implements RowMapper<Map<String, Object>> {
	public static final MapRowMapper INSTANCE = new MapRowMapper();

	public MapRowMapper() {
	}

	@Override
	public Class<Map<String, Object>> getType() {
		return null;
	}

	@Override
	public Map<String, Object> mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		Map<String, Object> map = new HashMap<>();
		for (int i = ONE; i <= meta.getColumnCount(); i++) {
			String name = meta.getColumnLabel(i);
			Object value = rs.getObject(i);
			map.put(name, value);
		}
		return map;
	}
}
