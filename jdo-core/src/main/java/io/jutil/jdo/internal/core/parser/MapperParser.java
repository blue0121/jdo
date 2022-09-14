package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.parser.ColumnMetadata;
import io.jutil.jdo.core.parser.FieldMetadata;
import io.jutil.jdo.core.parser.MapperMetadata;
import io.jutil.jdo.core.reflect.ReflectFactory;
import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.parser.model.DefaultColumnMetadata;
import io.jutil.jdo.internal.core.parser.model.DefaultMapperMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-05-12
 */
public class MapperParser extends AbstractParser {
	private static Logger logger = LoggerFactory.getLogger(MapperParser.class);

	public MapperParser(Dialect dialect) {
		super(dialect);
	}

    @Override
    public MapperMetadata parse(Class<?> clazz) {
		var classOperation = ReflectFactory.getClassOperation(clazz);
		var metadata = new DefaultMapperMetadata();
	    metadata.setClassOperation(classOperation);
	    Map<String, ColumnMetadata> columnMap = new LinkedHashMap<>();
		Map<String, FieldMetadata> fieldMap = new LinkedHashMap<>();

	    var fields = classOperation.getAllFields();
	    for (var entry : fields.entrySet()) {
		    var column = new DefaultColumnMetadata();
		    this.setFieldMetadata(entry.getValue(), column);
		    columnMap.put(column.getFieldName(), column);
			fieldMap.put(column.getFieldName(), column);
		    logger.debug("普通字段: {} <==> {}", column.getFieldName(), column.getColumnName());
	    }

	    metadata.setColumnMap(columnMap);
		metadata.setFieldMap(fieldMap);
	    metadata.check();
        return metadata;
    }
}
