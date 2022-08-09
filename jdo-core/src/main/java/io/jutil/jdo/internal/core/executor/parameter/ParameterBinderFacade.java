package io.jutil.jdo.internal.core.executor.parameter;

import io.jutil.jdo.internal.core.parser.ParserFacade;
import io.jutil.jdo.internal.core.sql.SqlParameter;
import io.jutil.jdo.internal.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class ParameterBinderFacade {
	private static Logger logger = LoggerFactory.getLogger(ParameterBinderFacade.class);

	private static final int ONE = 1;

	private final ParserFacade parserFacade;
	private final ConcurrentMap<Class<?>, ParameterBinder<?>> binderMap = new ConcurrentHashMap<>();
	private final Map<Class<?>, ParameterBindFactory<?>> factoryMap = new HashMap<>();

	public ParameterBinderFacade(ParserFacade parserFacade) {
		this.parserFacade = parserFacade;
		this.init();
		this.log();
	}

	private void init() {
		binderMap.put(BigDecimal.class, new BigDecimalBinder());
		binderMap.put(BigInteger.class, new BigIntegerBinder());
		binderMap.put(byte[].class, new ByteArrayBinder());

		binderMap.put(Byte.class, new ByteBinder());
		binderMap.put(Short.class, new ShortBinder());
		binderMap.put(Integer.class, new IntegerBinder());
		binderMap.put(Long.class, new LongBinder());
		binderMap.put(Float.class, new FloatBinder());
		binderMap.put(Double.class, new DoubleBinder());

		binderMap.put(LocalDate.class, new LocalDateBinder());
		binderMap.put(LocalTime.class, new LocalTimeBinder());
		binderMap.put(LocalDateTime.class, new LocalDateTimeBinder());
		binderMap.put(Instant.class, new InstantBinder());

		binderMap.put(Date.class, new DateBinder());
		binderMap.put(java.sql.Date.class, new SqlDateBinder());
		binderMap.put(Time.class, new SqlTimeBinder());
		binderMap.put(Timestamp.class, new SqlTimestampBinder());

		binderMap.put(String.class, new StringBinder());

		factoryMap.put(Enum.class, new EnumBindFactory());
		factoryMap.put(Object.class, new ObjectBindFactory(this));
	}

	private void log() {
		StringBuilder sb = new StringBuilder();
		sb.append("支持 PreparedStatement 参数绑定: {");
		for (var entry : binderMap.entrySet()) {
			sb.append(entry.getKey().getName())
					.append("=")
					.append(entry.getValue().getClass().getSimpleName())
					.append(",");

		}
		for (var entry : factoryMap.entrySet()) {
			sb.append(entry.getKey().getName())
					.append("=")
					.append(entry.getValue().getClass().getSimpleName())
					.append(",");
		}
		sb.delete(sb.length() - 1, sb.length());
		sb.append("}");
		logger.info(sb.toString());
	}

	@SuppressWarnings("unchecked")
	public void bind(PreparedStatement pstmt, List<SqlParameter> parameterList) throws SQLException {
		if (parameterList == null || parameterList.isEmpty()) {
			return;
		}
		int i = 1;
		for (var parameter : parameterList) {
			var value = parameter.getValue();
			if (ObjectUtil.isEmpty(value)) {
				pstmt.setObject(i, null);
			} else {
				var binder = this.getBinder(value.getClass());
				var context = BindContext.create(pstmt, i, parameter);
				binder.bind(context);
			}
			i++;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fetch(ResultSet rs, Class<T> clazz) throws SQLException {
		var binder = this.getBinder(clazz);
		List<T> objectList = new ArrayList<>();
		var rsmd = rs.getMetaData();
		while (rs.next()) {
			var context = FetchContext.create(rsmd, rs, ONE, null);
			T object = (T) binder.fetch(context);
			if (object != null) {
				objectList.add(object);
			}
		}
		return objectList;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	protected ParameterBinder getBinder(Class<?> clazz) {
		var binder = binderMap.get(clazz);
		if (binder != null) {
			return binder;
		}
		Class key = clazz;
		do {
			var factory = factoryMap.get(clazz);
			if (factory == null) {
				clazz = clazz.getSuperclass();
				continue;
			}
			return binderMap.computeIfAbsent(key, k -> factory.getBinder(key));
		} while (clazz != null);
		throw new UnsupportedOperationException("不支持类型: " + key.getName());
	}

	protected ParserFacade getParserFacade() {
		return parserFacade;
	}
}
