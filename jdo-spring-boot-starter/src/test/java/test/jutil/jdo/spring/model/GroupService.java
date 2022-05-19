package test.jutil.jdo.spring.model;

import io.jutil.jdo.spring.annotation.JdoTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2022-01-18
 */
@Service
@JdoTransactional
public class GroupService {
	private GroupDao groupDao;

	public GroupService() {
	}

	public void add(List<GroupEntity> groupList) {
		for (var group : groupList) {
			groupDao.save(group);
		}
	}

	public GroupEntity get(Integer id) {
		return groupDao.get(id);
	}

	public void saveList(List<GroupEntity> groupList) {
		if (groupList == null || groupList.isEmpty()) {
			return;
		}
		groupDao.saveList(groupList);
	}

	public int count(Map<String, Object> param) {
		return groupDao.count(param);
	}

	@Autowired
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
}
