package test.jutil.jdo.spring.it;

import io.jutil.jdo.core.collection.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.jutil.jdo.spring.model.GroupDao;
import test.jutil.jdo.spring.model.GroupEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2021-12-21
 */
public class GroupDaoTest extends BaseTest {
	private GroupDao groupDao;

	public GroupDaoTest() {
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
	public void testCrud() {
		GroupEntity group = GroupEntity.create(10, "red", 1);
		Assertions.assertEquals(1, groupDao.save(group));
		group = groupDao.get(10);
		GroupEntity.verify(group, 10, "red", 1);
		group.setName("redred");
		Assertions.assertEquals(1, groupDao.update(group));
		var map = groupDao.getList(List.of(1, 2, 10));
		GroupEntity.verify(map.get(1), 1, "blue", 1);
		GroupEntity.verify(map.get(2), 2, "green", 1);
		GroupEntity.verify(map.get(10), 10, "redred", 1);
		Assertions.assertEquals(3, groupDao.count(Map.of("count", 1)));
		Assertions.assertEquals(1, groupDao.deleteId(10));
		Assertions.assertEquals(2, groupDao.count(Map.of("count", 1)));
		Assertions.assertEquals(2, groupDao.deleteBy(Map.of("count", 1)));
		Assertions.assertEquals(0, groupDao.count(Map.of("count", 1)));
	}

	@Test
	public void testBatch() {
		List<GroupEntity> groupList = new ArrayList<>();
		int count = 5;
		List<Integer> idList = new ArrayList<>();
		for (int i = 10; i < count + 10; i++) {
			idList.add(i);
			groupList.add(GroupEntity.create(i, "name" + i, i));
		}
		groupDao.saveList(groupList);
		var map = groupDao.getList(idList);
		for (var id : idList) {
			GroupEntity.verify(map.get(id), id, "name" + id, id);
		}
		for (var group : groupList) {
			group.setName(group.getName() + group.getName());
		}
		groupDao.updateList(groupList);
		map = groupDao.getList(idList);
		for (var id : idList) {
			GroupEntity.verify(map.get(id), id, "name" + id + "name" + id, id);
		}
	}

	@Test
	public void testInc() {
		groupDao.inc(1, Map.of("count", 1));
		var group = groupDao.get(1);
		GroupEntity.verify(group, 1, "blue", 2);
	}

	@Test
	public void testGetObject() {
		var group = groupDao.getObject(Map.of("name", "blue"));
		GroupEntity.verify(group, 1, "blue", 1);
		int count = groupDao.getField(Integer.class, "count", Map.of("name", "blue"));
		Assertions.assertEquals(1, count);
	}

	@Test
	public void testPage() {
		List<GroupEntity> groupList = new ArrayList<>();
		int count = 10;
		List<Integer> idList = new ArrayList<>();
		for (int i = 10; i < count + 10; i++) {
			idList.add(i);
			groupList.add(GroupEntity.create(i, "name" + i, i));
		}
		groupDao.saveList(groupList);

		GroupEntity param = GroupEntity.create(null, "name", null);
		var list1 = groupDao.list(param);
		Assertions.assertEquals(10, list1.size());

		var list2 = groupDao.listPage(param, 0, 5);
		Assertions.assertEquals(5, list2.size());

		var list3 = groupDao.listPage(param, 10, 5);
		Assertions.assertEquals(0, list3.size());

		var page = new Page(5, 1);
		var page1 = groupDao.listPage(param, page);
		Assertions.assertEquals(10, page1.getTotalResult());
		Assertions.assertEquals(2, page1.getTotalPage());
		Assertions.assertEquals(1, page1.getPageIndex());
		Assertions.assertEquals(5, page1.getPageSize());
		Assertions.assertEquals(0, page1.getRowIndex());
		Assertions.assertEquals(5, page1.getResults().size());

		page.setPageIndex(2);
		var page2 = groupDao.listPage(param, page);
		Assertions.assertEquals(10, page2.getTotalResult());
		Assertions.assertEquals(2, page2.getTotalPage());
		Assertions.assertEquals(2, page2.getPageIndex());
		Assertions.assertEquals(5, page2.getPageSize());
		Assertions.assertEquals(5, page2.getRowIndex());
		Assertions.assertEquals(5, page2.getResults().size());

		page.setPageIndex(3);
		var page3 = groupDao.listPage(param, page);
		Assertions.assertEquals(10, page3.getTotalResult());
		Assertions.assertEquals(2, page3.getTotalPage());
		Assertions.assertEquals(3, page3.getPageIndex());
		Assertions.assertEquals(5, page3.getPageSize());
		Assertions.assertEquals(10, page3.getRowIndex());
		Assertions.assertEquals(0, page3.getResults().size());
	}

	@Autowired
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
}
