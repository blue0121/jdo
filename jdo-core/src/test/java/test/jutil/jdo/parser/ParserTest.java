package test.jutil.jdo.parser;

import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.parser.ColumnMetadata;
import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.core.parser.FieldMetadata;
import io.jutil.jdo.core.parser.IdMetadata;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.core.parser.SqlItem;
import io.jutil.jdo.core.parser.VersionMetadata;
import org.junit.jupiter.api.Assertions;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-13
 */
public abstract class ParserTest {
	protected ParserTest() {
	}

    protected void checkTable(EntityMetadata metadata, String table) {
        Assertions.assertNotNull(metadata);
        Assertions.assertEquals(table, metadata.getTableName());
    }

    protected void checkId(IdMetadata metadata, IdType idType, GeneratorType generatorType,
                           String field, String column) {
        this.checkField(metadata, field, column);
        Assertions.assertEquals(idType, metadata.getIdType());
        Assertions.assertEquals(generatorType, metadata.getGeneratorType());
    }

    protected void checkColumn(ColumnMetadata metadata, boolean mustInsert, boolean mustUpdate,
                               String field, String column) {
        this.checkField(metadata, field, column);
        Assertions.assertEquals(mustInsert, metadata.isMustInsert());
        Assertions.assertEquals(mustUpdate, metadata.isMustUpdate());
    }

    protected void checkVersion(VersionMetadata metadata, boolean force, int defaultValue,
                                String field, String column) {
        this.checkField(metadata, field, column);
        Assertions.assertEquals(force, metadata.isForce());
        Assertions.assertEquals(defaultValue, metadata.getDefaultValue());
    }

    protected void checkField(FieldMetadata metadata, String field, String column) {
        Assertions.assertNotNull(metadata);
        Assertions.assertEquals(field, metadata.getFieldName());
        Assertions.assertEquals(column, metadata.getColumnName());
    }

    protected void checkSql(SqlItem item, String sql, List<String> parameterNameList) {
        Assertions.assertNotNull(item);
        Assertions.assertEquals(sql, item.getSql());
        Assertions.assertEquals(parameterNameList, item.getParameterNameList());
    }

}
