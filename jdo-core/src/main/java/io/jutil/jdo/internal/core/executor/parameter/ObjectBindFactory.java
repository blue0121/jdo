package io.jutil.jdo.internal.core.executor.parameter;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.MapperMetadata;
import io.jutil.jdo.core.reflect.ClassFieldOperation;
import io.jutil.jdo.core.reflect.ClassOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		private final MapperMetadata metadata;
		private final Class<Object> clazz;
		private final ClassOperation classOperation;
		private final Map<String, ClassFieldOperation> fieldMap = new HashMap<>();

		public ObjectBinder(ParameterBinderFacade binderFacade, Class<Object> clazz) {
			this.binderFacade = binderFacade;
			this.metadata = binderFacade.getParserFacade().getMetadataCache().load(clazz);
			this.clazz = clazz;
			this.classOperation = metadata.getClassOperation();

			for (var entry : metadata.getFieldMap().entrySet()) {
				var field = entry.getValue();
				fieldMap.put(field.getColumnName().toLowerCase(), field.getFieldOperation());
				fieldMap.put(field.getColumnName().toUpperCase(), field.getFieldOperation());
			}
		}

		@Override
		public Class<Object> getType() {
			return clazz;
		}

		@Override
		public void bind(BindContext<Object> context) throws SQLException {
			throw new UnsupportedOperationException("不支持类型: " + clazz.getName());
		}

		@Override
		public Object fetch(FetchContext context) throws SQLException {
			Object object = classOperation.newInstanceQuietly();
			if (object == null) {
				throw new JdbcException("无法实例化: " + classOperation.getName());
			}

			var rsmd = context.getResultSetMetaData();
			var rs = context.getResultSet();
			for (int j = 1; j <= rsmd.getColumnCount(); j++) {
				String label = rsmd.getColumnLabel(j);
				var field = fieldMap.get(label);
				if (field != null) {
					Class<?> type = field.getType();
					var binder = binderFacade.getBinder(type);
					var ctx = FetchContext.create(rsmd, rs, j, null);
					var value = binder.fetch(ctx);
					field.setFieldValue(object, value);
				}
			}

			return object;
		}

	}
}
