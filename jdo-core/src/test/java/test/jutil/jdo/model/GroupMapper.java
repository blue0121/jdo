package test.jutil.jdo.model;

import io.jutil.jdo.core.annotation.Mapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
@Getter
@Setter
@NoArgsConstructor
@Mapper
public class GroupMapper {
	private Integer id;
	private String name;
	private Integer count;

	public static GroupMapper create(Integer id, String name, Integer count) {
		GroupMapper group = new GroupMapper();
		group.setId(id);
		group.setName(name);
		group.setCount(count);
		return group;
	}

	public static void verify(GroupMapper group, Integer id, String name, Integer count) {
		Assertions.assertNotNull(group);
		Assertions.assertEquals(id, group.getId());
		Assertions.assertEquals(name, group.getName());
		Assertions.assertEquals(count, group.getCount());
	}

}
