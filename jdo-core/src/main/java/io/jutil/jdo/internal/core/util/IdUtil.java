package io.jutil.jdo.internal.core.util;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser2.EntityMetadata;
import io.jutil.jdo.core.parser2.IdMetadata;
import io.jutil.jdo.core.parser2.IdType;
import io.jutil.jdo.internal.core.executor.KeyHolder;

import java.util.Collection;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class IdUtil {
	private IdUtil() {
	}

	public static void setId(KeyHolder holder, Object target, EntityMetadata metadata) {
		if (holder == null || target == null || metadata == null) {
			return;
		}
		var id = metadata.getIdMetadata();
		if (id == null) {
			return;
		}
		if (target instanceof Collection<?> list) {
			int i = 0;
			var numList = holder.getKeyList();
			if (numList.isEmpty()) {
				return;
			}
			for (var t : list) {
				setId(numList.get(i++), t, id);
			}
		} else {
			setId(holder.getKey(), target, id);
		}
	}

	private static void setId(Number num, Object target, IdMetadata id) {
		if (num == null) {
			return;
		}
		var field = id.getFieldOperation();
		if (id.getIdType() == IdType.INT) {
			field.setFieldValue(target, num.intValue());
		} else if (id.getIdType() == IdType.LONG) {
			field.setFieldValue(target, num.longValue());
		}
	}

	public static IdMetadata checkSingleId(EntityMetadata entityMetadata) {
		var id = entityMetadata.getIdMetadata();
		if (id == null) {
			throw new JdbcException(entityMetadata.getTargetClass().getName() + " @Id主键个数大于1，不支持该操作");
		}

		return id;
	}

}
