package test.jutil.jdo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
@Getter
@Setter
public class User {
	private Integer id;
	private String name;
	private String password;
	private Double score;
	private LocalDate birthday;
	private Date loginTime;
	private Integer state;

	public User() {
	}

}
