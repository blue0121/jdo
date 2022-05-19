package test.jutil.jdo.spring.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.jutil.jdo.spring.model.State;
import test.jutil.jdo.spring.model.UserEntity;
import test.jutil.jdo.spring.model.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2022-01-18
 */
public class UserServiceTest extends BaseTest {
	private UserService userService;

	public UserServiceTest() {
	}

	@BeforeEach
	public void beforeEach() {
		super.beforeEach();
	}

	@AfterEach
	public void afterEach() {
		super.afterEach();
	}

	@Test
	public void testSaveList() {
		String str = "name";
		int count = 10;
		List<UserEntity> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			var user = UserEntity.create(i, str, str, State.ACTIVE);
			list.add(user);
		}
		userService.saveList(list);
		Assertions.assertEquals(count, userService.count(Map.of("name", str)));
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
