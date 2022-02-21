package test.jutil.jdo.model;

import io.jutil.jdo.core.annotation.Mapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
@Getter
@Setter
@NoArgsConstructor
@Mapper
public class UserGroupMapper {
	private Integer id;
	private Integer groupId;
	private String name;
	private String password;
	private State state;
	private String groupName;

}
