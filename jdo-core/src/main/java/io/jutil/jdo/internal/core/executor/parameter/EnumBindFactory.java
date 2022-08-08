package io.jutil.jdo.internal.core.executor.parameter;

import io.jutil.jdo.internal.core.util.EnumUtil;
import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-13
 */
@NoArgsConstructor
public class EnumBindFactory implements ParameterBindFactory<Enum<?>> {


	@Override
	public ParameterBinder<Enum<?>> getBinder(Class<Enum<?>> clazz) {
		return new EnumBinder(clazz);
	}

	private class EnumBinder implements ParameterBinder<Enum<?>> {
		private final Class<Enum<?>> clazz;

		public EnumBinder(Class<Enum<?>> clazz) {
			this.clazz = clazz;
		}

		@Override
		public Class<Enum<?>> getType() {
			return clazz;
		}

		@Override
		public void bind(BindContext<Enum<?>> context) throws SQLException {
			var pstmt = context.getPreparedStatement();
			var i = context.getIndex();
			var value = context.getValue();
			pstmt.setString(i, value.name());
		}

		@Override
		public Enum<?> fetch(FetchContext context) throws SQLException {
			var rs = context.getResultSet();
			var i = context.getIndex();
			var str = rs.getString(i);
			return EnumUtil.fromString(clazz, str);
		}

		@Override
		public void bind(PreparedStatement pstmt, int i, Enum<?> val) throws SQLException {
			pstmt.setString(i, val.name());
		}

		@Override
		public Enum<?> fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
			var str = rs.getString(i);
			return EnumUtil.fromString(clazz, str);
		}
	}
}
