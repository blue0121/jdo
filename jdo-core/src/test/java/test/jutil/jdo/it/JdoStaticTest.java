package test.jutil.jdo.it;

import io.jutil.jdo.core.exception.VersionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.model.GroupMapper;
import test.jutil.jdo.model.State;
import test.jutil.jdo.model.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-02
 */
public class JdoStaticTest extends BaseTest {
	public JdoStaticTest() {
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
	public void testGet() {
		var group = jdoTemplate.get(GroupEntity.class, 1);
		GroupEntity.verify(group, 1, "blue", 1);
	}

	@Test
	public void testGetList() {
		List<Integer> idList = List.of(1, 2);
		Map<Integer, GroupEntity> map = jdoTemplate.getList(GroupEntity.class, idList);
		Assertions.assertEquals(2, map.size());
		var g1 = map.get(1);
		GroupEntity.verify(g1, 1, "blue", 1);
		GroupEntity g2 = map.get(2);
		GroupEntity.verify(g2, 2, "green", 1);
	}

	@Test
	public void testDeleteId() {
		Assertions.assertEquals(1, jdoTemplate.deleteId(GroupEntity.class, 1));
		Assertions.assertEquals(1, jdoTemplate.deleteId(GroupEntity.class, 2));
		Assertions.assertNull(jdoTemplate.get(GroupEntity.class, 1));
		Assertions.assertNull(jdoTemplate.get(GroupEntity.class, 2));
	}

	@Test
	public void testDeleteIdList() {
		Assertions.assertEquals(2, jdoTemplate.deleteIdList(GroupEntity.class, List.of(1, 2)));
		Assertions.assertNull(jdoTemplate.get(GroupEntity.class, 1));
		Assertions.assertNull(jdoTemplate.get(GroupEntity.class, 2));
	}

	@Test
	public void testDeleteIdList2() {
		Assertions.assertEquals(2, jdoTemplate.deleteIdList(GroupEntity.class, List.of(1, 2, 3)));
		Assertions.assertNull(jdoTemplate.get(GroupEntity.class, 1));
		Assertions.assertNull(jdoTemplate.get(GroupEntity.class, 2));
	}

	@Test
	public void testSave() {
		var group = GroupEntity.create(3, "red", 3);
		jdoTemplate.save(group, false);
		var group2 = jdoTemplate.get(GroupEntity.class, 3);
		GroupEntity.verify(group2, 3, "red", 3);

		var user = UserEntity.create(1, "group1", "pass1", State.ACTIVE);
		int c = jdoTemplate.save(user, false);
		Assertions.assertTrue(c > 0);
		Assertions.assertNotNull(user.getId());
		System.out.println("userId: " + user.getId());
		var user2 = jdoTemplate.get(UserEntity.class, user.getId());
		UserEntity.verify(user2, 1, 1, "group1", "pass1", State.ACTIVE);
	}

	@Test
	public void testSaveAndUpdateList1() {
		String name = "update_name_";
		int count = 5;
		int[] expectRs = new int[count];
		List<GroupEntity> groupList = new ArrayList<>();
		List<Integer> idList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			expectRs[i] = 1;
			int id = i + 3;
			idList.add(id);
			groupList.add(GroupEntity.create(id, "red_" + id, id));
		}
		int[] groupRs = jdoTemplate.saveList(groupList);
		Assertions.assertArrayEquals(expectRs, groupRs);
		int j = 1;
		for (var group : groupList) {
			group.setName(name + j);
			j++;
		}
		groupRs = jdoTemplate.updateList(groupList);
		Assertions.assertArrayEquals(expectRs, groupRs);
		var groupMap = jdoTemplate.getList(GroupEntity.class, idList);
		for (j = 1; j <= 5; j++) {
			int id = j + 2;
			var g = groupMap.get(id);
			GroupEntity.verify(g, id, name + j, id);
		}
	}

	@Test
	public void testSaveAndUpdateList2() {
		String name = "update_name_";
		int count = 5;
		int[] expectRs = new int[count];
		List<UserEntity> userList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			expectRs[i] = 1;
			int id = i + 1;
			userList.add(UserEntity.create(id, "red_" + id, "pwd_" + id, State.ACTIVE));
		}
		int[] userRs = jdoTemplate.saveList(userList);
		Assertions.assertArrayEquals(expectRs, userRs);
		List<Integer> idList = new ArrayList<>();
		for (var user : userList) {
			Assertions.assertNotNull(user.getId());
			idList.add(user.getId());
		}
		System.out.println("userIds: " + idList);

		int j = 1;
		for (var user : userList) {
			user.setName(name + j);
			user.setPassword(name + j);
			j++;
		}
		userRs = jdoTemplate.updateList(userList);
		Assertions.assertArrayEquals(expectRs, userRs);
		var userMap = jdoTemplate.getList(UserEntity.class, idList);
		System.out.println(userMap);
		j = 1;
		for (var id : idList) {
			UserEntity.verify(userMap.get(id), 2, j, name + j, name + j, State.ACTIVE);
			j++;
		}

		userList.get(1).setVersion(100);
		Assertions.assertThrows(VersionException.class, () -> jdoTemplate.updateList(userList));
	}

	@Test
	public void testUpdate() {
		String name = "update_name";
		var group1 = jdoTemplate.get(GroupEntity.class, 1);
		GroupEntity.verify(group1, 1, "blue", 1);
		group1.setName(name);
		Assertions.assertEquals(1, jdoTemplate.update(group1, false));
		group1 = jdoTemplate.get(GroupEntity.class, 1);
		GroupEntity.verify(group1, 1, name, 1);

		var user1 = UserEntity.create(1, "blue", "pwd", State.INACTIVE);
		Assertions.assertEquals(1, jdoTemplate.save(user1, false));
		user1 = jdoTemplate.get(UserEntity.class, user1.getId());
		UserEntity.verify(user1, 1, 1, "blue", "pwd", State.INACTIVE);
		user1.setName(name);
		user1.setPassword(name);
		Assertions.assertEquals(1, jdoTemplate.update(user1, false));
		user1 = jdoTemplate.get(UserEntity.class, user1.getId());
		UserEntity.verify(user1, 2, 1, name, name, State.INACTIVE);

		var user2 = UserEntity.create(1, "blue", "blue", State.ACTIVE);
		user2.setId(user1.getId());
		Assertions.assertThrows(VersionException.class, () -> jdoTemplate.update(user2, false));
	}

	@Test
	public void testGetObject() {
		String sql = "select count(*) from \"group\"";
		Integer n = jdoTemplate.getObject(Integer.class, sql);
		Assertions.assertEquals(2, n.intValue());

		sql += " where \"name\"=?";
		n = jdoTemplate.getObject(Integer.class, sql, List.of("blue"));
		Assertions.assertEquals(1, n.intValue());

		sql = "select * from \"group\" where \"name\"=?";
		GroupMapper group = jdoTemplate.getObject(GroupMapper.class, sql, List.of("blue"));
		GroupMapper.verify(group, 1, "blue", 1);

		sql = "select \"id\" from \"group\" where \"name\"=?";
		GroupMapper group2 = jdoTemplate.getObject(GroupMapper.class, sql, List.of("blue"));
		Assertions.assertNotNull(group2);
		Assertions.assertEquals(1, group2.getId().intValue());
		Assertions.assertNull(group2.getName());
		Assertions.assertNull(group2.getCount());
	}

	@Test
	public void testList() {
		String sql = "select \"id\" from \"group\"";
		List<Integer> list = jdoTemplate.list(Integer.class, sql);
		Assertions.assertTrue(list != null && list.size() == 2);
		Assertions.assertTrue(list.contains(1));
		Assertions.assertTrue(list.contains(2));

		sql = "select * from \"group\" order by \"id\"";
		List<GroupMapper> groupList = jdoTemplate.list(GroupMapper.class, sql);
		Assertions.assertTrue(groupList != null && groupList.size() == 2);
		GroupMapper.verify(groupList.get(0), 1, "blue", 1);
		GroupMapper.verify(groupList.get(1), 2, "green", 1);
	}

}
