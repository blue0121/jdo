package test.jutil.jdo.parser;

import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.parser.FieldConfig;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.internal.core.dialect.MySQLDialect;
import io.jutil.jdo.internal.core.parser.ParserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.UserEntity;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class EntityParserTest {

	public EntityParserTest() {
	}

	@Test
	public void testParse() {
		ParserFactory factory = new ParserFactory(new MySQLDialect(), true);
		factory.parse(UserEntity.class);
		var cache = factory.getConfigCache();
		var config = cache.loadEntityConfig(UserEntity.class);

		Assertions.assertEquals(UserEntity.class, config.getClazz());
		Assertions.assertEquals("usr_user", config.getTableName());
		Assertions.assertEquals("`usr_user`", config.getEscapeTableName());

		var idMap = config.getIdMap();
		var columnMap = config.getColumnMap();
		var extraMap = config.getExtraMap();
		Assertions.assertEquals(1, idMap.size());
		Assertions.assertEquals(4, columnMap.size());
		Assertions.assertEquals(1, extraMap.size());

		var id = config.getIdConfig();
		Assertions.assertNotNull(id);
		Assertions.assertEquals(IdType.INT, id.getIdType());
		Assertions.assertEquals(GeneratorType.INCREMENT, id.getGeneratorType());
		this.checkField(config.getIdConfig(), "id", "id", "`id`");

		this.checkField(config.getVersionConfig(), "version", "version", "`version`");
		this.checkField(columnMap.get("groupId"), "groupId", "group_id", "`group_id`");
		this.checkField(columnMap.get("name"), "name", "name", "`name`");
		this.checkField(columnMap.get("password"), "password", "password", "`password`");
		this.checkField(columnMap.get("state"), "state", "state", "`state`");
		this.checkField(extraMap.get("groupName"), "groupName", "group_name", "`group_name`");

		var sql = config.getSqlConfig();
		this.checkSql(sql.getSelectById(), "select * from `usr_user` where `id`=?", List.of("id"));
		this.checkSql(sql.getSelectByIdList(), "select * from `usr_user` where `id` in (%s)", List.of("id"));
		this.checkSql(sql.getInsert(), "insert into `usr_user` (`version`,`group_id`,`name`,`password`,`state`) values (?,?,?,?,?)", List.of("version","groupId","name","password","state"));
		this.checkSql(sql.getUpdateById(), "update `usr_user` set `group_id`=?,`name`=?,`password`=?,`state`=? where `id`=?", List.of("groupId","name","password","state","id"));
		this.checkSql(sql.getUpdateByIdAndVersion(), "update `usr_user` set `group_id`=?,`name`=?,`password`=?,`state`=?,`version`=`version`+1 where `id`=? and `version`=?", List.of("groupId","name","password","state","id","version"));
		this.checkSql(sql.getDeleteById(), "delete from `usr_user` where `id`=?", List.of("id"));
		this.checkSql(sql.getDeleteByIdList(), "delete from `usr_user` where `id` in (%s)", List.of("id"));
	}

	@Test
	public void testParse2() {
		ParserFactory factory = new ParserFactory(new MySQLDialect(), false);
		factory.parse(UserEntity.class);
		var cache = factory.getConfigCache();
		var config = cache.loadEntityConfig(UserEntity.class);

		Assertions.assertEquals(UserEntity.class, config.getClazz());
		Assertions.assertEquals("usr_user", config.getTableName());
		Assertions.assertEquals("usr_user", config.getEscapeTableName());

		var idMap = config.getIdMap();
		var columnMap = config.getColumnMap();
		var extraMap = config.getExtraMap();
		Assertions.assertEquals(1, idMap.size());
		Assertions.assertEquals(4, columnMap.size());
		Assertions.assertEquals(1, extraMap.size());

		var id = config.getIdConfig();
		Assertions.assertNotNull(id);
		Assertions.assertEquals(IdType.INT, id.getIdType());
		Assertions.assertEquals(GeneratorType.INCREMENT, id.getGeneratorType());
		this.checkField(config.getIdConfig(), "id", "id", "id");

		this.checkField(config.getVersionConfig(), "version", "version", "version");
		this.checkField(columnMap.get("groupId"), "groupId", "group_id", "group_id");
		this.checkField(columnMap.get("name"), "name", "name", "name");
		this.checkField(columnMap.get("password"), "password", "password", "password");
		this.checkField(columnMap.get("state"), "state", "state", "state");
		this.checkField(extraMap.get("groupName"), "groupName", "group_name", "group_name");

		var sql = config.getSqlConfig();
		this.checkSql(sql.getSelectById(), "select * from usr_user where id=?", List.of("id"));
		this.checkSql(sql.getSelectByIdList(), "select * from usr_user where id in (%s)", List.of("id"));
		this.checkSql(sql.getInsert(), "insert into usr_user (version,group_id,name,password,state) values (?,?,?,?,?)", List.of("version","groupId","name","password","state"));
		this.checkSql(sql.getUpdateById(), "update usr_user set group_id=?,name=?,password=?,state=? where id=?", List.of("groupId","name","password","state","id"));
		this.checkSql(sql.getUpdateByIdAndVersion(), "update usr_user set group_id=?,name=?,password=?,state=?,version=version+1 where id=? and version=?", List.of("groupId","name","password","state","id","version"));
		this.checkSql(sql.getDeleteById(), "delete from usr_user where id=?", List.of("id"));
		this.checkSql(sql.getDeleteByIdList(), "delete from usr_user where id in (%s)", List.of("id"));
	}


	private void checkField(FieldConfig config, String field, String column, String escapeColumn) {
		Assertions.assertNotNull(config);
		Assertions.assertEquals(field, config.getFieldName());
		Assertions.assertEquals(column, config.getColumnName());
		Assertions.assertEquals(escapeColumn, config.getEscapeColumnName());
	}

	private void checkSql(SqlItem item, String sql, List<String> paramNameList) {
		Assertions.assertEquals(sql, item.getSql());
		Assertions.assertEquals(paramNameList, item.getParamNameList());
	}

}
