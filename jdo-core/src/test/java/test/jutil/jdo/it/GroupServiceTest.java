package test.jutil.jdo.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupDao;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.model.GroupService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-17
 */
public class GroupServiceTest extends BaseTest {
    private GroupService groupService;

	public GroupServiceTest() {
	}

	@BeforeEach
	public void beforeEach() {
		super.beforeEach();
		var groupDao = new GroupDao();
		groupDao.setJdoTemplate(jdoTemplate);

		groupService = new GroupService();
		groupService.setGroupDao(groupDao);
		groupService.setTransactionManager(jdo.getTransactionManager());
	}

	@AfterEach
	public void afterEach() {
		super.afterEach();
	}

	@Test
	public void testTransactional() {
		int id = 10;
		List<GroupEntity> list = new ArrayList<>();
		list.add(GroupEntity.create(id, "name10", id));
		list.add(GroupEntity.create(id + 1, "name11", id + 1));
		list.add(GroupEntity.create(id, "name10", id));
		try {
			groupService.add(list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assertions.assertNull(groupService.get(id + 1));
		Assertions.assertNull(groupService.get(id));
	}

}
