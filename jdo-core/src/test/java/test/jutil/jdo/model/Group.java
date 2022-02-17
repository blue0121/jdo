package test.jutil.jdo.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
@Getter
@Setter
public class Group {
	private Integer id;
	private String name;
	private Integer type;
	private Integer state;

	public Group() {
	}

	public Group(Integer id, String name, Integer type, Integer state) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.state = state;
	}

}
