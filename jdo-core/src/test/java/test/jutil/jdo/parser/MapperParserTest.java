package test.jutil.jdo.parser;

import io.jutil.jdo.core.annotation.Column;
import io.jutil.jdo.core.annotation.Mapper;
import io.jutil.jdo.core.parser.MetadataType;
import io.jutil.jdo.internal.core.dialect.MySQLDialect;
import io.jutil.jdo.internal.core.parser.ParserFacade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author Jin Zheng
 * @since 2022-05-13
 */
public class MapperParserTest extends ParserTest {
	public MapperParserTest() {
	}

	@ParameterizedTest
	@CsvSource({"test.jutil.jdo.parser.MapperParserTest$CommonClass",
			"test.jutil.jdo.parser.MapperParserTest$CommonClass2"})
	public void testParse(Class<?> clazz) {
		var facade = new ParserFacade(new MySQLDialect());
		var metadata = facade.parse(clazz);

		Assertions.assertNotNull(metadata);
		Assertions.assertEquals(MetadataType.MAPPER, metadata.getMetadataType());
		Assertions.assertEquals(clazz, metadata.getTargetClass());

		var columnMap = metadata.getColumnMap();
		Assertions.assertEquals(3, columnMap.size());

		this.checkField(columnMap.get("groupId"), "groupId", "group_id");
		this.checkField(columnMap.get("username"), "username", "username");
		this.checkField(columnMap.get("password"), "password", "pwd");
	}

    @Getter
    @Setter
    @NoArgsConstructor
    @Mapper
    public class CommonClass {
	    private Integer groupId;
	    private String username;
		@Column(name = "pwd")
        private String password;
    }

	@NoArgsConstructor
	@Mapper
	public class CommonClass2 {
		public Integer groupId;
		public String username;
		@Column(name = "pwd")
		public String password;
	}

}
