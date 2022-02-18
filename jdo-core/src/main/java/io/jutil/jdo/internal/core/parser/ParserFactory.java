package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.Mapper;
import io.jutil.jdo.core.reflect.JavaBean;
import io.jutil.jdo.internal.core.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class ParserFactory {
	private static Logger logger = LoggerFactory.getLogger(ParserFactory.class);
	private static Set<Class<? extends Annotation>> annotationSet = Set.of(
			Entity.class, Mapper.class);

	private final Dialect dialect;
	private final Map<Class<? extends Annotation>, Parser> parserMap = new HashMap<>();
	private final EntityConfigCache entityConfigCache = new EntityConfigCache();
	private final MapperConfigCache mapperConfigCache = new MapperConfigCache();

	public ParserFactory(Dialect dialect) {
		this.dialect = dialect;
		parserMap.put(Entity.class, new EntityParser(dialect, entityConfigCache));
		parserMap.put(Mapper.class, new MapperParser(dialect, mapperConfigCache));
	}

	public void parse(Class<?> clazz) {
		JavaBean javaBean = JavaBean.create(clazz);
		var annotationList = javaBean.getAnnotations();
		for (var annotation : annotationList) {
			if (!annotationSet.contains(annotation.annotationType()))
				continue;

			Parser parser = parserMap.get(annotation.annotationType());
			if (parser != null) {
				logger.debug("找到 {} 处理器: {}", annotation.annotationType().getSimpleName(), parser);
				parser.parse(javaBean);
				break;
			}
		}
	}

	public Dialect getDialect() {
		return dialect;
	}

	public EntityConfigCache getEntityConfigCache() {
		return entityConfigCache;
	}

	public MapperConfigCache getMapperConfigCache() {
		return mapperConfigCache;
	}

}
