package io.jutil.jdo.internal.core.executor.parameter;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.FieldConfig;
import io.jutil.jdo.core.reflect.BeanField;
import io.jutil.jdo.core.reflect.JavaBean;
import io.jutil.jdo.internal.core.parser.ConfigCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-14
 */
public class ObjectBindFactory implements ParameterBindFactory<Object> {
	private final ParameterBinderFacade binderFacade;

	public ObjectBindFactory(ParameterBinderFacade binderFacade) {
		this.binderFacade = binderFacade;
	}

	@Override
	public ParameterBinder<Object> getBinder(Class<Object> clazz) {
		return new ObjectBinder(binderFacade, clazz);
	}

	private class ObjectBinder implements ParameterBinder<Object> {
		private static Logger logger = LoggerFactory.getLogger(ObjectBindFactory.class);

		private final ParameterBinderFacade binderFacade;
		private final ConfigCache configCache;
		private final Class<Object> clazz;
		private final JavaBean javaBean;
		private final Map<String, BeanField> fieldMap = new HashMap<>();

		public ObjectBinder(ParameterBinderFacade binderFacade, Class<Object> clazz) {
			this.binderFacade = binderFacade;
			this.configCache = binderFacade.getParserFactory().getConfigCache();
			this.clazz = clazz;
			var config = configCache.load(clazz);
			this.javaBean = config.getJavaBean();
			config.getColumnMap().forEach((k, v) -> this.setFieldMap(v));
			if (config instanceof EntityConfig c) {
				c.getIdMap().forEach((k, v) -> this.setFieldMap(v));
				this.setFieldMap(c.getVersionConfig());
				c.getExtraMap().forEach((k, v) -> this.setFieldMap(v));
			}
		}

		@Override
		public Class<Object> getType() {
			return clazz;
		}

		@Override
		public void bind(PreparedStatement pstmt, int i, Object val) throws SQLException {
			throw new UnsupportedOperationException("不支持类型: " + clazz.getName());
		}

		private void setFieldMap(FieldConfig field) {
			if (field == null) {
				return;
			}

			fieldMap.put(field.getColumnName().toLowerCase(), field.getBeanField());
			fieldMap.put(field.getColumnName().toUpperCase(), field.getBeanField());
		}

		@Override
		public Object fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
			Object object = javaBean.newInstanceQuietly();
			if (object == null) {
				throw new JdbcException("无法实例化: " + javaBean.getName());
			}

			for (int j = 1; j <= rsmd.getColumnCount(); j++) {
				String label = rsmd.getColumnLabel(j);
				BeanField field = fieldMap.get(label);
				if (field != null) {
					Class<?> type = field.getField().getType();
					var binder = binderFacade.getBinder(type);
					if (logger.isDebugEnabled()) {
						logger.debug("找到 [{}] ParameterBinder: {}", type.getSimpleName(), binder.getClass().getSimpleName());
					}
					var value = binder.fetch(rsmd, rs, j);
					field.setFieldValue(object, value);
				}
			}

			return object;
		}
	}
}
