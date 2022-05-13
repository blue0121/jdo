package io.jutil.jdo.internal.core.parser2;

import io.jutil.jdo.core.parser2.ColumnMetadata;
import io.jutil.jdo.core.parser2.MapperMetadata;
import io.jutil.jdo.core.reflect2.ReflectFactory;
import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.parser2.model.DefaultColumnMetadata;
import io.jutil.jdo.internal.core.parser2.model.DefaultMapperMetadata;
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

	public MapperParser(Dialect dialect, boolean escape) {
		super(dialect, escape);
	}

    @Override
    public MapperMetadata parse(Class<?> clazz) {
		var classOperation = ReflectFactory.getClassOperation(clazz);
		var metadata = new DefaultMapperMetadata();
	    metadata.setClassOperation(classOperation);
	    Map<String, ColumnMetadata> columnMap = new LinkedHashMap<>();

	    var fieldMap = classOperation.getAllFields();
	    for (var entry : fieldMap.entrySet()) {
		    var column = new DefaultColumnMetadata();
		    this.setFieldMetadata(entry.getValue(), column);
		    columnMap.put(column.getFieldName(), column);
		    logger.debug("普通字段: {} <==> {}", column.getFieldName(), column.getColumnName());
	    }

	    metadata.setColumnMap(columnMap);
	    metadata.check();
        return metadata;
    }
}
