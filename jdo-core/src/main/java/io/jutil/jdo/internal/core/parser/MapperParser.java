package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.parser.ColumnConfig;
import io.jutil.jdo.core.reflect.JavaBean;
import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.parser.model.DefaultColumnConfig;
import io.jutil.jdo.internal.core.parser.model.DefaultMapperConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class MapperParser extends AbstractParser {
	private static Logger logger = LoggerFactory.getLogger(MapperParser.class);

	private final MapperConfigCache cache;

	public MapperParser(Dialect dialect, MapperConfigCache cache) {
		super(dialect);
		this.cache = cache;
	}

	@Override
	protected void parseInternal(JavaBean bean) {
		logger.info("Mapped class: {}", bean.getTargetClass().getName());
		DefaultMapperConfig config = new DefaultMapperConfig();
		config.setJavaBean(bean);
		Map<String, ColumnConfig> columnMap = new LinkedHashMap<>();

		var fieldMap = bean.getAllFields();
		for (var entry : fieldMap.entrySet()) {
			DefaultColumnConfig column = new DefaultColumnConfig();
			this.setFieldConfig(entry.getValue(), column);
			columnMap.put(column.getFieldName(), column);
			logger.debug("Column: {} <==> {}", column.getFieldName(), column.getColumnName());
		}

		config.setColumnMap(columnMap);
		config.check();
		cache.put(config);
	}

}
