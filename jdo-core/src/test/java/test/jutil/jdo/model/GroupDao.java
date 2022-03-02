package test.jutil.jdo.model;


import io.jutil.jdo.core.engine.BaseDao;
import io.jutil.jdo.core.engine.Expression;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-03-02
 */
public class GroupDao extends BaseDao<GroupEntity> {
	public GroupDao() {
	}

	@Override
	protected void query(Expression exp, GroupEntity object, List<Object> paramList) {
		if (object.getName() != null && !object.getName().isEmpty()) {
			exp.add("""
					a."name" like ?""");
			paramList.add("%" + object.getName() + "%");
		}
		if (object.getCount() != null) {
			exp.add("""
					a."count"=?""");
			paramList.add(object.getCount());
		}
	}
}
