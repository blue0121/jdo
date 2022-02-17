package test.jutil.jdo.model;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class TestModel {
	private String username;
	private String password;

	public TestModel() {
	}

	public String getUsername() {
		return username;
	}

	@CoreTest
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	@CoreTest
	public void setPassword(String password) {
		this.password = password;
	}
}
