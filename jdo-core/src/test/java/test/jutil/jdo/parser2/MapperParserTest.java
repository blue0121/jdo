package test.jutil.jdo.parser2;

import io.jutil.jdo.core.annotation.Column;
import io.jutil.jdo.core.annotation.Mapper;
import io.jutil.jdo.core.parser2.MetadataType;
import io.jutil.jdo.internal.core.dialect.MySQLDialect;
import io.jutil.jdo.internal.core.parser2.ParserFacade;
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
	@CsvSource({"test.jutil.jdo.parser2.MapperParserTest$CommonClass,true",
			"test.jutil.jdo.parser2.MapperParserTest$CommonClass2,true",
			"test.jutil.jdo.parser2.MapperParserTest$CommonClass,false",
			"test.jutil.jdo.parser2.MapperParserTest$CommonClass2,false"})
	public void testParse(Class<?> clazz, boolean isEscape) {
		var facade = new ParserFacade(new MySQLDialect(), isEscape);
		var metadata = facade.parse(clazz);

		Assertions.assertNotNull(metadata);
		Assertions.assertEquals(MetadataType.MAPPER, metadata.getMetadataType());
		Assertions.assertEquals(clazz, metadata.getTargetClass());

		var columnMap = metadata.getColumnMap();
		Assertions.assertEquals(3, columnMap.size());

		this.checkField(columnMap.get("groupId"), "groupId", "group_id", this.escape("group_id", isEscape));
		this.checkField(columnMap.get("username"), "username", "username", this.escape("username", isEscape));
		this.checkField(columnMap.get("password"), "password", "pwd", this.escape("pwd", isEscape));
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
