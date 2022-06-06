package test.jutil.jdo.parser;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.core.annotation.Transient;
import io.jutil.jdo.core.annotation.Version;
import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.internal.core.dialect.MySQLDialect;
import io.jutil.jdo.internal.core.parser.ParserFacade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-13
 */
public class EntityParserTest extends ParserTest {
	public EntityParserTest() {
	}

	@ParameterizedTest
	@CsvSource({"test.jutil.jdo.parser.EntityParserTest$CommonClass,true",
			"test.jutil.jdo.parser.EntityParserTest$CommonClass2,true",
			"test.jutil.jdo.parser.EntityParserTest$CommonClass,false",
			"test.jutil.jdo.parser.EntityParserTest$CommonClass2,false"})
	public void testParse(Class<?> clazz, boolean isEscape) {
		var facade = new ParserFacade(new MySQLDialect(), isEscape);
		var metadata = (EntityMetadata) facade.parse(clazz);
		this.checkTable(metadata, "c_common_class", this.escape("c_common_class", isEscape));

		var idMap = metadata.getIdMap();
		var columnMap = metadata.getColumnMap();
		var transientMap = metadata.getTransientMap();
		var fieldMap = metadata.getFieldMap();
		Assertions.assertEquals(1, idMap.size());
		Assertions.assertEquals(2, columnMap.size());
		Assertions.assertEquals(1, transientMap.size());
		Assertions.assertEquals(5, fieldMap.size());

		this.checkId(idMap.get("id"), IdType.INT, GeneratorType.INCREMENT,
				"id", "id", this.escape("id", isEscape));
		this.checkId(metadata.getIdMetadata(), IdType.INT, GeneratorType.INCREMENT,
				"id", "id", this.escape("id", isEscape));
		this.checkVersion(metadata.getVersionMetadata(), true, 1,
				"version", "version", this.escape("version", isEscape));
		this.checkColumn(columnMap.get("groupId"), false, false,
				"groupId", "group_id", this.escape("group_id", isEscape));
		this.checkColumn(columnMap.get("username"), false, false,
				"username", "username", this.escape("username", isEscape));
		this.checkField(transientMap.get("groupName"), "groupName", "group_name",
				this.escape("group_name", isEscape));

		var sql = metadata.getSqlMetadata();
		if (isEscape) {
			this.checkSql(sql.getSelectById(), "select * from `c_common_class` where `id`=?", List.of("id"));
			this.checkSql(sql.getSelectByIdList(), "select * from `c_common_class` where `id` in (%s)", List.of("id"));
			this.checkSql(sql.getInsert(), "insert into `c_common_class` (`version`,`group_id`,`username`) values (?,?,?)", List.of("version","groupId","username"));
			this.checkSql(sql.getUpdateById(), "update `c_common_class` set `group_id`=?,`username`=? where `id`=?", List.of("groupId","username","id"));
			this.checkSql(sql.getUpdateByIdAndVersion(), "update `c_common_class` set `group_id`=?,`username`=?,`version`=`version`+1 where `id`=? and `version`=?", List.of("groupId","username","id","version"));
			this.checkSql(sql.getDeleteById(), "delete from `c_common_class` where `id`=?", List.of("id"));
			this.checkSql(sql.getDeleteByIdList(), "delete from `c_common_class` where `id` in (%s)", List.of("id"));
		} else {
			this.checkSql(sql.getSelectById(), "select * from c_common_class where id=?", List.of("id"));
			this.checkSql(sql.getSelectByIdList(), "select * from c_common_class where id in (%s)", List.of("id"));
			this.checkSql(sql.getInsert(), "insert into c_common_class (version,group_id,username) values (?,?,?)", List.of("version","groupId","username"));
			this.checkSql(sql.getUpdateById(), "update c_common_class set group_id=?,username=? where id=?", List.of("groupId","username","id"));
			this.checkSql(sql.getUpdateByIdAndVersion(), "update c_common_class set group_id=?,username=?,version=version+1 where id=? and version=?", List.of("groupId","username","id","version"));
			this.checkSql(sql.getDeleteById(), "delete from c_common_class where id=?", List.of("id"));
			this.checkSql(sql.getDeleteByIdList(), "delete from c_common_class where id in (%s)", List.of("id"));
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@Entity(table = "c_common_class")
    public class CommonClass {
		@Id
		private Integer id;
		@Version
		private Integer version;
		private Integer groupId;
		private String username;
		@Transient
		private String groupName;
    }

	@NoArgsConstructor
	@Entity(table = "c_common_class")
	public class CommonClass2 {
		@Id
		public Integer id;
		@Version
		public Integer version;
		public Integer groupId;
		public String username;
		@Transient
		public String groupName;
	}

}
