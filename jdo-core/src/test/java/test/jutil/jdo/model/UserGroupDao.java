package test.jutil.jdo.model;

import io.jutil.jdo.core.engine.Expression;
import io.jutil.jdo.core.engine.OrderBy;
import io.jutil.jdo.core.engine.QueryDao;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-03-02
 */

public class UserGroupDao extends QueryDao<UserEntity, UserGroupMapper> {
	public UserGroupDao() {
	}

	@Override
	protected StringBuilder selectCount() {
		StringBuilder sql = new StringBuilder("""
				select count(*) group_name from usr_user a \
				left join usr_group g on g.id=a.group_id""");
		return sql;
	}

	@Override
	protected StringBuilder select() {
		StringBuilder sql = new StringBuilder("""
				select a.*, g.name group_name from usr_user a \
				left join usr_group g on g.id=a.group_id""");
		return sql;
	}

	@Override
	protected void query(Expression exp, UserEntity object, List<Object> paramList) {
		if (object.getGroupId() != null) {
			exp.add("a.group_id=?");
			paramList.add(object.getGroupId());
		}
		if (object.getName() != null && !object.getName().isEmpty()) {
			exp.add("a.name like ?");
			paramList.add("%" + object.getName() + "%");
		}
		if (object.getState() != null) {
			exp.add("a.state=?");
			paramList.add(object.getState());
		}
	}

	@Override
	protected void orderBy(OrderBy order, UserEntity object) {
		order.add("a.id asc");
	}
}
