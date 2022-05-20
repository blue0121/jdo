package test.jutil.jdo.spring.model;

import io.jutil.jdo.core.engine.Expression;
import io.jutil.jdo.spring.engine.SpringBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2021-12-21
 */
@Repository
public class GroupDao extends SpringBaseDao<GroupEntity> {
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
