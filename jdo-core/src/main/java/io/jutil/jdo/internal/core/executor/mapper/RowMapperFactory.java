package io.jutil.jdo.internal.core.executor.mapper;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.MapperConfig;
import io.jutil.jdo.internal.core.parser.EntityConfigCache;
import io.jutil.jdo.internal.core.parser.MapperConfigCache;
import io.jutil.jdo.internal.core.parser.ParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class RowMapperFactory {
	private static Logger logger = LoggerFactory.getLogger(RowMapperFactory.class);

	private final EntityConfigCache entityConfigCache;
	private final MapperConfigCache mapperConfigCache;
	private final ConcurrentMap<Class<?>, RowMapper<?>> rowMapper = new ConcurrentHashMap<>();

	public RowMapperFactory(ParserFactory parserFactory) {
		this.entityConfigCache = parserFactory.getEntityConfigCache();
		this.mapperConfigCache = parserFactory.getMapperConfigCache();
		this.init();
	}

	private void init() {
		rowMapper.put(BigDecimal.class, new BigDecimalRowMapper());
		rowMapper.put(Double.class, new DoubleRowMapper());
		rowMapper.put(Float.class, new FloatRowMapper());
		rowMapper.put(Integer.class, new IntegerRowMapper());
		rowMapper.put(LocalDate.class, new LocalDateRowMapper());
		rowMapper.put(LocalDateTime.class, new LocalDateTimeRowMapper());
		rowMapper.put(LocalTime.class, new LocalTimeRowMapper());
		rowMapper.put(Long.class, new LongRowMapper());
		rowMapper.put(Map.class, new MapRowMapper());
		rowMapper.put(Date.class, new SqlDateRowMapper());
		rowMapper.put(Time.class, new SqlTimeRowMapper());
		rowMapper.put(Timestamp.class, new SqlTimeRowMapper());
		rowMapper.put(java.util.Date.class, new SqlDateRowMapper());
		rowMapper.put(String.class, new StringRowMapper());
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getObjectList(Object object, ResultSet rs) throws SQLException {
		if (object instanceof Class c) {
			return this.getObjectList(c, rs);
		}
		if (object instanceof EntityConfig c) {
			RowMapper mapper = rowMapper.computeIfAbsent(c.getClazz(), k -> new ObjectRowMapper<T>(c));
			return this.getObjectList(mapper, rs);
		}
		if (object instanceof MapperConfig c) {
			RowMapper mapper = rowMapper.computeIfAbsent(c.getClazz(), k -> new ObjectRowMapper<T>(c));
			return this.getObjectList(mapper, rs);
		}
		throw new UnsupportedOperationException("Unknown type: " + object.getClass());
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> getObjectList(Class<T> clazz, ResultSet rs) throws SQLException {
		RowMapper mapper = rowMapper.computeIfAbsent(clazz, k -> {
			if (entityConfigCache.exist(k)) {
				return new ObjectRowMapper<T>(entityConfigCache.get(k));
			}
			if (mapperConfigCache.exist(k)) {
				return new ObjectRowMapper<T>(mapperConfigCache.get(k));
			}
			return null;
		});
		if (mapper == null) {
			throw new UnsupportedOperationException("Unknown class: " + clazz.getName());
		}
		logger.debug("找到 [{}] RowMapper: {}", clazz.getSimpleName(), mapper.getClass().getSimpleName());
		return this.getObjectList(mapper, rs);
	}

	private <T> List<T> getObjectList(RowMapper<T> mapper, ResultSet rs) throws SQLException {
		List<T> objectList = new ArrayList<>();
		int row = 0;
		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next()) {
			T object = mapper.mapRow(meta, rs, row);
			objectList.add(object);
			row++;
		}
		return objectList;
	}

}
