package test.jutil.jdo.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
@Getter
@Setter
public class TestModel2 {
	@CoreTest
	private String username;
	@CoreTest
	private String password;

	public TestModel2() {
	}

}
