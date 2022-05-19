package test.jutil.jdo.spring.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.jutil.jdo.spring.model.GroupEntity;
import test.jutil.jdo.spring.model.GroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2022-01-18
 */
public class GroupServiceTest extends BaseTest {
    private GroupService groupService;

	public GroupServiceTest() {
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
		List<GroupEntity> list = new ArrayList<>();
		for (int i = 10; i > 0; i--) {
			list.add(GroupEntity.create(i, str, count));
		}
		try {
			groupService.saveList(list);
			Assertions.fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assertions.assertEquals(0, groupService.count(Map.of("name", str)));
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

	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
}
