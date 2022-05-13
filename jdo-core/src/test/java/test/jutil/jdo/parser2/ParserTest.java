package test.jutil.jdo.parser2;

import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.parser2.ColumnMetadata;
import io.jutil.jdo.core.parser2.EntityMetadata;
import io.jutil.jdo.core.parser2.FieldMetadata;
import io.jutil.jdo.core.parser2.IdMetadata;
import io.jutil.jdo.core.parser2.IdType;
import io.jutil.jdo.core.parser2.SqlItem;
import io.jutil.jdo.core.parser2.VersionMetadata;
import org.junit.jupiter.api.Assertions;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-05-13
 */
public abstract class ParserTest {
	protected ParserTest() {
	}

    protected String escape(String column, boolean escape) {
        if (!escape) {
            return column;
        }
        return "`" + column + "`";
    }

    protected void checkTable(EntityMetadata metadata, String table, String escape) {
        Assertions.assertNotNull(metadata);
        Assertions.assertEquals(table, metadata.getTableName());
        Assertions.assertEquals(escape, metadata.getEscapeTableName());
    }

    protected void checkId(IdMetadata metadata, IdType idType, GeneratorType generatorType,
                           String field, String column, String escape) {
        this.checkField(metadata, field, column, escape);
        Assertions.assertEquals(idType, metadata.getIdType());
        Assertions.assertEquals(generatorType, metadata.getGeneratorType());
    }

    protected void checkColumn(ColumnMetadata metadata, boolean mustInsert, boolean mustUpdate,
                               String field, String column, String escape) {
        this.checkField(metadata, field, column, escape);
        Assertions.assertEquals(mustInsert, metadata.isMustInsert());
        Assertions.assertEquals(mustUpdate, metadata.isMustUpdate());
    }

    protected void checkVersion(VersionMetadata metadata, boolean force, int defaultValue,
                                String field, String column, String escape) {
        this.checkField(metadata, field, column, escape);
        Assertions.assertEquals(force, metadata.isForce());
        Assertions.assertEquals(defaultValue, metadata.getDefaultValue());
    }

    protected void checkField(FieldMetadata metadata, String field, String column, String escape) {
        Assertions.assertNotNull(metadata);
        Assertions.assertEquals(field, metadata.getFieldName());
        Assertions.assertEquals(column, metadata.getColumnName());
        Assertions.assertEquals(escape, metadata.getEscapeColumnName());
    }

    protected void checkSql(SqlItem item, String sql, List<String> parameterNameList) {
        Assertions.assertNotNull(item);
        Assertions.assertEquals(sql, item.getSql());
        Assertions.assertEquals(parameterNameList, item.getParameterNameList());
    }

}
