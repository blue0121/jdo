package io.jutil.jdo.core.reflect;

/**
 * 列名操作
 *
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface ColumnNameOperation extends NameOperation {
	/**
	 * 字段 => 列名，类名 => 表名
	 *
	 * @return
	 */
	default String getColumnName() {
		String name = this.getName();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				if (i > 0) {
					sb.append('_');
				}

				sb.append(Character.toLowerCase(c));
			}
			else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
