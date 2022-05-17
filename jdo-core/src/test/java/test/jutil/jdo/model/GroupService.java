package test.jutil.jdo.model;

import io.jutil.jdo.core.engine.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-17
 */
public class GroupService {
	private static Logger logger = LoggerFactory.getLogger(GroupService.class);

    private GroupDao groupDao;
    private TransactionManager transactionManager;

	public GroupService() {
	}

	public void add(List<GroupEntity> groupList) {
		transactionManager.begin();
		try {
			for (var group : groupList) {
				groupDao.save(group);
			}
			transactionManager.commit();
		} catch (Exception e) {
			transactionManager.rollback();
			throw e;
		}
	}

	public GroupEntity get(Integer id) {
		return groupDao.get(id);
	}

	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
