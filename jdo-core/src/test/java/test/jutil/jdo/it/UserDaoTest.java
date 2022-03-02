package test.jutil.jdo.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.State;
import test.jutil.jdo.model.UserDao;
import test.jutil.jdo.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-03-02
 */
public class UserDaoTest extends BaseTest {
	private UserDao userDao;

	public UserDaoTest() {
	}

	@BeforeEach
	public void beforeEach() {
		super.beforeEach();
		userDao = new UserDao();
		userDao.setJdoTemplate(jdoTemplate);
	}

	@AfterEach
	public void afterEach() {
		super.afterEach();
	}

	@Test
	public void testBatch() {
		int count = 10;
		List<UserEntity> userList = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			var user = UserEntity.create(i, "name" + i, "pwd" + i, State.ACTIVE);
			userList.add(user);
		}
		userDao.saveList(userList);
		List<Integer> idList = new ArrayList<>();
		for (var user : userList) {
			Assertions.assertNotNull(user.getId());
			idList.add(user.getId());
			user.setName(user.getName() + user.getName());
			user.setPassword(user.getPassword() + user.getPassword());
		}
		userDao.updateList(userList);

		var map = userDao.getList(idList);
		int i = 1;
		for (var id : idList) {
			UserEntity.verify(map.get(id), 2, i, "name" + i + "name" + i, "pwd" + i + "pwd" + i, State.ACTIVE);
			i++;
		}

		var param1 = UserEntity.create(1, null, null, null);
		Assertions.assertEquals(1, userDao.getTotalResult(param1));
		var page1 = userDao.listPage(param1, null);
		Assertions.assertEquals(1, page1.getTotalPage());
		Assertions.assertEquals(1, page1.getTotalResult());
		Assertions.assertEquals(1, page1.getResults().size());
		List<UserEntity> list1 = page1.getResults();
		var user1 = list1.get(0);
		Assertions.assertEquals(1, user1.getGroupId());
		Assertions.assertEquals("blue", user1.getGroupName());

		var param2 = UserEntity.create(10, null, null, null);
		Assertions.assertEquals(1, userDao.getTotalResult(param2));
		var page2 = userDao.listPage(param2, null);
		Assertions.assertEquals(1, page2.getTotalPage());
		Assertions.assertEquals(1, page2.getTotalResult());
		Assertions.assertEquals(1, page2.getResults().size());
		List<UserEntity> list2 = page2.getResults();
		var user2 = list2.get(0);
		Assertions.assertEquals(10, user2.getGroupId());
		Assertions.assertNull(user2.getGroupName());
	}

}
