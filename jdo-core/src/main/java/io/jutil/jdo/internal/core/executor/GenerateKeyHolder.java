package io.jutil.jdo.internal.core.executor;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
@NoArgsConstructor
public class GenerateKeyHolder implements KeyHolder {
	private static Logger logger = LoggerFactory.getLogger(GenerateKeyHolder.class);

	private List<Number> keyList;


	@Override
	public Number getKey() {
		if (keyList == null || keyList.isEmpty()) {
			return null;
		}
		return keyList.get(0);
	}

	@Override
	public List<Number> getKeyList() {
		if (keyList == null) {
			return List.of();
		}
		return keyList;
	}

	@Override
	public void mapRow(ResultSet rs) throws SQLException {
		if (rs == null) {
			return;
		}
		List<Number> list = new ArrayList<>();
		var meta = rs.getMetaData();
		logger.debug("KeyHolder 列数: {}", meta.getColumnCount());
		while (rs.next()) {
			if (meta.getColumnCount() == 0) {
				continue;
			}
			long key = rs.getLong(1);
			Long num = rs.wasNull() ? null : key;
			list.add(num);
		}
		this.keyList = List.copyOf(list);
	}
}
