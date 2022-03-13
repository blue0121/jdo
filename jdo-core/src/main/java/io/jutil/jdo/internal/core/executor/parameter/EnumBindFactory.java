package io.jutil.jdo.internal.core.executor.parameter;

import io.jutil.jdo.internal.core.util.EnumUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-13
 */
public class EnumBindFactory implements ParameterBindFactory<Enum<?>> {

	public EnumBindFactory() {
	}

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
		public void bind(PreparedStatement pstmt, int i, Enum<?> val) throws SQLException {
			pstmt.setString(i, val.name());
		}

		@Override
		public Enum<?> fetch(ResultSet rs, int i) throws SQLException {
			var str = rs.getString(i);
			return EnumUtil.fromString(clazz, str);
		}
	}
}
