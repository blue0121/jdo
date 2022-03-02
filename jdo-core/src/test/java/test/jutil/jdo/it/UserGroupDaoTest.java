package test.jutil.jdo.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.State;
import test.jutil.jdo.model.UserDao;
import test.jutil.jdo.model.UserEntity;
import test.jutil.jdo.model.UserGroupDao;
import test.jutil.jdo.model.UserGroupMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-03-02
 */
public class UserGroupDaoTest extends BaseTest {
	private UserDao userDao;
	private UserGroupDao userGroupDao;

	public UserGroupDaoTest() {
	}

	@BeforeEach
	public void beforeEach() {
		super.beforeEach();
		userDao = new UserDao();
		userDao.setJdoTemplate(jdoTemplate);
		userGroupDao = new UserGroupDao();
		userGroupDao.setJdoTemplate(jdoTemplate);
	}

	@AfterEach
	public void afterEach() {
		super.afterEach();
	}

	@Test
	public void testQuery() {
		int count = 2;
		List<UserEntity> userList = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			var user = UserEntity.create(i, "name" + i, "pwd" + i, State.ACTIVE);
			userList.add(user);
		}
		userDao.saveList(userList);

		var param1 = UserEntity.create(1, "name", null, null);
		var param2 = UserEntity.create(2, "name", null, null);
		Assertions.assertEquals(1, userGroupDao.getTotalResult(param1));
		Assertions.assertEquals(1, userGroupDao.getTotalResult(param2));

		var page1 = userGroupDao.listPage(param1, null);
		Assertions.assertEquals(1, page1.getTotalPage());
		Assertions.assertEquals(1, page1.getTotalResult());
		Assertions.assertEquals(1, page1.getResults().size());
		List<UserGroupMapper> list1 = page1.getResults();
		var userGroup1 = list1.get(0);
		Assertions.assertEquals(1, userGroup1.getGroupId());
		Assertions.assertEquals("blue", userGroup1.getGroupName());

		var page2 = userGroupDao.listPage(param2, null);
		Assertions.assertEquals(1, page2.getTotalPage());
		Assertions.assertEquals(1, page2.getTotalResult());
		Assertions.assertEquals(1, page2.getResults().size());
		List<UserGroupMapper> list2 = page2.getResults();
		var userGroup2 = list2.get(0);
		Assertions.assertEquals(2, userGroup2.getGroupId());
		Assertions.assertEquals("green", userGroup2.getGroupName());
	}

}
