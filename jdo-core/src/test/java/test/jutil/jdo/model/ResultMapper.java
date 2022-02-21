package test.jutil.jdo.model;

import io.jutil.jdo.core.annotation.Column;
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
public class ResultMapper {
	private String userId;
	private String name;
	@Column(name = "message")
	private String msg;

}
