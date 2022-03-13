package io.jutil.jdo.internal.core.executor.parameter;

import io.jutil.jdo.internal.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class ParameterBinderFacade {
	private static Logger logger = LoggerFactory.getLogger(ParameterBinderFacade.class);

	private final ConcurrentMap<Class<?>, ParameterBinder<?>> binderMap = new ConcurrentHashMap<>();
	private final Map<Class<?>, ParameterBindFactory<?>> factoryMap = new HashMap<>();

	private final ParameterBinder<?> defaultBinder = new ObjectBinder();

	public ParameterBinderFacade() {
		this.init();
		this.log();
	}

	private void init() {
		binderMap.put(BigDecimal.class, new BigDecimalBinder());
		binderMap.put(BigInteger.class, new BingIntegerBinder());
		binderMap.put(byte[].class, new ByteArrayBinder());
		binderMap.put(Byte.class, new ByteBinder());
		binderMap.put(Date.class, new DateBinder());
		binderMap.put(Double.class, new DoubleBinder());
		binderMap.put(Float.class, new FloatBinder());
		binderMap.put(Integer.class, new IntegerBinder());
		binderMap.put(LocalDate.class, new LocalDateBinder());
		binderMap.put(LocalDateTime.class, new LocalDateTimeBinder());
		binderMap.put(LocalTime.class, new LocalTimeBinder());
		binderMap.put(Long.class, new LongBinder());
		binderMap.put(Short.class, new ShortBinder());
		binderMap.put(java.sql.Date.class, new SqlDateBinder());
		binderMap.put(Time.class, new SqlTimeBinder());
		binderMap.put(Timestamp.class, new SqlTimestampBinder());
		binderMap.put(String.class, new StringBinder());

		factoryMap.put(Enum.class, new EnumBindFactory());
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
	public void bind(PreparedStatement pstmt, Collection<?> paramList) throws SQLException {
		if (paramList == null || paramList.isEmpty()) {
			return;
		}
		int i = 1;
		for (var param : paramList) {
			if (ObjectUtil.isEmpty(param)) {
				pstmt.setObject(i, null);
			} else {
				var binder = this.getBinder(param.getClass());
				if (logger.isDebugEnabled()) {
					logger.debug("找到 [{}] ParameterBinder: {}", param.getClass().getSimpleName(),
							binder.getClass().getSimpleName());
				}
				binder.bind(pstmt, i, param);
			}
			i++;
		}
	}


	@SuppressWarnings("rawtypes")
	private ParameterBinder getBinder(Class<?> clazz) {
		var binder = this.getBinderFromFactory(clazz);
		if (binder == null) {
			return defaultBinder;
		}
		return binder;
	}

	@SuppressWarnings("rawtypes")
	private ParameterBinder getBinderFromFactory(Class<?> clazz) {
		var binder = binderMap.get(clazz);
		if (binder !=  null) {
			return binder;
		}
		while (clazz != Object.class) {
			var factory = factoryMap.get(clazz);
			if (factory == null) {
				clazz = clazz.getSuperclass();
				continue;
			}
			Class key = clazz;
			return binderMap.computeIfAbsent(key, k -> factory.getBinder(key));
		}
		return null;
	}

}
