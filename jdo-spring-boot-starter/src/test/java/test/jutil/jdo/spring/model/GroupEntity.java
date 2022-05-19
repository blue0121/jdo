package test.jutil.jdo.spring.model;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.annotation.Id;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
@Getter
@Setter
@Entity(table = "group")
public class GroupEntity {
	@Id(generator = GeneratorType.ASSIGNED)
	private Integer id;
	private String name;
	private Integer count;

	public GroupEntity() {
	}

	public static GroupEntity create(Integer id, String name, Integer count) {
		GroupEntity group = new GroupEntity();
		group.setId(id);
		group.setName(name);
		group.setCount(count);
		return group;
	}

	public static void verify(GroupEntity group, Integer id, String name, Integer count) {
		Assertions.assertNotNull(group);
		Assertions.assertEquals(id, group.getId());
		Assertions.assertEquals(name, group.getName());
		Assertions.assertEquals(count, group.getCount());
	}

}
