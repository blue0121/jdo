package test.jutil.jdo.it;

import io.jutil.jdo.core.exception.VersionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.model.State;
import test.jutil.jdo.model.UserEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-02
 */
public class JdoDynamicTest extends BaseTest {
	public JdoDynamicTest() {
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
	public void testCount() {
		Map<String, Object> param = Map.of("name", "blue");
		int count = jdoTemplate.count(GroupEntity.class, param);
		Assertions.assertEquals(1, count);

		param = Map.of("name", "blue", "count", 2);
		count = jdoTemplate.count(GroupEntity.class, param);
		Assertions.assertEquals(0, count);
	}

	@Test
	public void testDeleteBy() {
		Map<String, Object> param = Map.of("name", "blue");
		int count = jdoTemplate.deleteBy(GroupEntity.class, param);
		Assertions.assertEquals(1, count);

		param = Map.of("name", "green", "count", 1);
		count = jdoTemplate.deleteBy(GroupEntity.class, param);
		Assertions.assertEquals(1, count);

		var map = jdoTemplate.getList(GroupEntity.class, List.of(1, 2));
		Assertions.assertTrue(map.isEmpty());
	}

	@Test
	public void testExist() {
		GroupEntity group = new GroupEntity();
		group.setName("blue");
		Assertions.assertTrue(jdoTemplate.exist(group, "name"));

		group.setId(1);
		Assertions.assertFalse(jdoTemplate.exist(group, "name"));

		group.setId(2);
		Assertions.assertTrue(jdoTemplate.exist(group, "name"));
	}

	@Test
	public void testGetField() {
		Map<String, Object> param = new HashMap<>();
		param.put("id", 1);
		Assertions.assertEquals("blue", jdoTemplate.getField(GroupEntity.class, String.class, "name", param));

		param.put("count", 1);
		Assertions.assertEquals("blue", jdoTemplate.getField(GroupEntity.class, String.class, "name", param));

		param.put("id", 10);
		Assertions.assertNull(jdoTemplate.getField(GroupEntity.class, String.class, "name", param));
	}

	@Test
	public void testGet() {
		Map<String, Object> param = new HashMap<>();
		param.put("id", 1);
		var group = jdoTemplate.getObject(GroupEntity.class, param);
		GroupEntity.verify(group, 1, "blue", 1);

		param.put("name", "blue");
		group = jdoTemplate.getObject(GroupEntity.class, param);
		GroupEntity.verify(group, 1, "blue", 1);

		param.put("id", 10);
		Assertions.assertNull(jdoTemplate.getObject(GroupEntity.class, param));
	}

	@Test
	public void testInc1() {
		Map<String, Integer> param = new HashMap<>();
		param.put("count", 10);
		int n = jdoTemplate.inc(GroupEntity.class, 1, param);
		Assertions.assertEquals(1, n);
		var group = jdoTemplate.get(GroupEntity.class, 1);
		GroupEntity.verify(group, 1, "blue", 11);


		n = jdoTemplate.inc(GroupEntity.class, 10, param);
		Assertions.assertEquals(0, n);
	}

	@Test
	public void testInc2() {
		var user = UserEntity.create(1, "blue", "blue", State.ACTIVE);
		int n = jdoTemplate.insert(user, false);
		Assertions.assertEquals(1, n);

		Map<String, Integer> param = new HashMap<>();
		param.put("groupId", 10);
		n = jdoTemplate.inc(UserEntity.class, user.getId(), param);
		Assertions.assertEquals(1, n);

		user = jdoTemplate.get(UserEntity.class, user.getId());
		UserEntity.verify(user, 1, 11, "blue", "blue", State.ACTIVE);
	}

	@Test
	public void testSaveObject() {
		Map<String, Object> map = Map.of("id", 3, "name", "red", "count", 1);
		int n = jdoTemplate.insertObject(GroupEntity.class, map);
		Assertions.assertEquals(1, n);
		var group = jdoTemplate.get(GroupEntity.class, 3);
		GroupEntity.verify(group, 3, "red", 1);

		map = Map.of("groupId", 1, "name", "blue", "password", "blue", "state", State.ACTIVE);
		n = jdoTemplate.insertObject(UserEntity.class, map);
		Assertions.assertEquals(1, n);
		var user = jdoTemplate.getObject(UserEntity.class, Map.of("name", "blue"));
		System.out.println("userId: " + user.getId());
		UserEntity.verify(user, 1, 1, "blue", "blue", State.ACTIVE);
	}

	@Test
	public void testSave() {
		UserEntity user = UserEntity.create(1, "blue", "blue", State.ACTIVE);
		int n = jdoTemplate.insert(user, true);
		Assertions.assertEquals(1, n);
		Assertions.assertNotNull(user.getId());
		user = jdoTemplate.get(UserEntity.class, user.getId());
		UserEntity.verify(user, 1, 1, "blue", "blue", State.ACTIVE);
	}

	@Test
	public void testUpdateObject() {
		Map<String, Object> map = Map.of("name", "blueblue");
		int n = jdoTemplate.updateObject(GroupEntity.class, 1, map);
		Assertions.assertEquals(1, n);
		var group = jdoTemplate.get(GroupEntity.class, 1);
		GroupEntity.verify(group, 1, "blueblue", 1);

		var user = UserEntity.create(1, "blue", "blue", State.ACTIVE);
		n = jdoTemplate.insert(user);
		Assertions.assertEquals(1, n);
		map = Map.of("name", "blueblue", "version", 1);
		n = jdoTemplate.updateObject(UserEntity.class, user.getId(), map);
		Assertions.assertEquals(1, n);
		user = jdoTemplate.get(UserEntity.class, user.getId());
		UserEntity.verify(user, 2, 1, "blueblue", "blue", State.ACTIVE);

		map = Map.of("name", "blueblue", "version", 1);
		var updateUser = user;
		var updateMap = map;
		Assertions.assertThrows(VersionException.class,
				() -> jdoTemplate.updateObject(UserEntity.class, updateUser.getId(), updateMap),
				"test.jutil.jdo.model.UserEntity 版本冲突");
	}

	@Test
	public void testUpdate() {
		var group = GroupEntity.create(1, "blueblue", null);
		int n = jdoTemplate.update(group);
		Assertions.assertEquals(1, n);
		group = jdoTemplate.get(GroupEntity.class, 1);
		GroupEntity.verify(group, 1, "blueblue", 1);

		var user = UserEntity.create(1, "blue", "blue", State.ACTIVE);
		n = jdoTemplate.insert(user);
		Assertions.assertEquals(1, n);
		user = jdoTemplate.get(UserEntity.class, user.getId());
		user.setName("blueblue");
		n = jdoTemplate.update(user);
		Assertions.assertEquals(1, n);
		user = jdoTemplate.get(UserEntity.class, user.getId());
		UserEntity.verify(user, 2, 1, "blueblue", "blue", State.ACTIVE);

		user.setVersion(1);
		var updateUser = user;
		Assertions.assertThrows(VersionException.class, () -> jdoTemplate.update(updateUser),
				"test.jutil.jdo.model.UserEntity 版本冲突");
	}

}
