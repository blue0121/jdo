package test.jutil.jdo.parser;

import io.jutil.jdo.internal.core.dialect.MySQLDialect;
import io.jutil.jdo.internal.core.parser.ParserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.ResultMapper;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class MapperParserTest {

	public MapperParserTest() {
	}

	@Test
	public void testParse() {
		ParserFactory factory = new ParserFactory(new MySQLDialect(), true);
		factory.parse(ResultMapper.class);
		var cache = factory.getMapperConfigCache();
		var config = cache.get(ResultMapper.class);

		Assertions.assertEquals(ResultMapper.class, config.getClazz());
		var map = config.getColumnMap();
		Assertions.assertEquals(3, map.size());

		var userIdColumn = map.get("userId");
		Assertions.assertNotNull(userIdColumn);
		Assertions.assertEquals("userId", userIdColumn.getFieldName());
		Assertions.assertEquals("user_id", userIdColumn.getColumnName());
		Assertions.assertEquals("`user_id`", userIdColumn.getEscapeColumnName());

		var nameColumn = map.get("name");
		Assertions.assertNotNull(nameColumn);
		Assertions.assertEquals("name", nameColumn.getFieldName());
		Assertions.assertEquals("name", nameColumn.getColumnName());
		Assertions.assertEquals("`name`", nameColumn.getEscapeColumnName());

		var msgColumn = map.get("msg");
		Assertions.assertNotNull(msgColumn);
		Assertions.assertEquals("msg", msgColumn.getFieldName());
		Assertions.assertEquals("message", msgColumn.getColumnName());
		Assertions.assertEquals("`message`", msgColumn.getEscapeColumnName());
	}

	@Test
	public void testParse2() {
		ParserFactory factory = new ParserFactory(new MySQLDialect(), false);
		factory.parse(ResultMapper.class);
		var cache = factory.getMapperConfigCache();
		var config = cache.get(ResultMapper.class);

		Assertions.assertEquals(ResultMapper.class, config.getClazz());
		var map = config.getColumnMap();
		Assertions.assertEquals(3, map.size());

		var userIdColumn = map.get("userId");
		Assertions.assertNotNull(userIdColumn);
		Assertions.assertEquals("userId", userIdColumn.getFieldName());
		Assertions.assertEquals("user_id", userIdColumn.getColumnName());
		Assertions.assertEquals("user_id", userIdColumn.getEscapeColumnName());

		var nameColumn = map.get("name");
		Assertions.assertNotNull(nameColumn);
		Assertions.assertEquals("name", nameColumn.getFieldName());
		Assertions.assertEquals("name", nameColumn.getColumnName());
		Assertions.assertEquals("name", nameColumn.getEscapeColumnName());

		var msgColumn = map.get("msg");
		Assertions.assertNotNull(msgColumn);
		Assertions.assertEquals("msg", msgColumn.getFieldName());
		Assertions.assertEquals("message", msgColumn.getColumnName());
		Assertions.assertEquals("message", msgColumn.getEscapeColumnName());
	}

}
