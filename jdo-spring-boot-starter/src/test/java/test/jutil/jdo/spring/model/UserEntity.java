package test.jutil.jdo.spring.model;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.core.annotation.Transient;
import io.jutil.jdo.core.annotation.Version;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
@Getter
@Setter
@Entity(table = "usr_user")
public class UserEntity {
	@Id
	private Integer id;
	@Version
	private Integer version;
	private Integer groupId;
	private String name;
	private String password;
	private State state;
	@Transient
	private String groupName;

	public UserEntity() {
	}

	public static UserEntity create(Integer groupId, String name, String password, State state) {
		UserEntity user = new UserEntity();
		user.setGroupId(groupId);
		user.setName(name);
		user.setPassword(password);
		user.setState(state);
		return user;
	}

	public static void verify(UserEntity user, Integer version, Integer groupId, String name, String password, State state) {
		Assertions.assertNotNull(user);
		Assertions.assertEquals(version, user.getVersion());
		Assertions.assertEquals(groupId, user.getGroupId());
		Assertions.assertEquals(name, user.getName());
		Assertions.assertEquals(password, user.getPassword());
		Assertions.assertEquals(state, user.getState());
	}

}
