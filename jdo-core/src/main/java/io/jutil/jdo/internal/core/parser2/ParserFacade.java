package io.jutil.jdo.internal.core.parser2;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.Mapper;
import io.jutil.jdo.core.parser2.MapperMetadata;
import io.jutil.jdo.internal.core.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-05-12
 */
public class ParserFacade implements Parser {
    private static Logger logger = LoggerFactory.getLogger(ParserFacade.class);
    private static Set<Class<? extends Annotation>> annotationSet = Set.of(Entity.class, Mapper.class);

    private final Dialect dialect;
    private final Map<Class<? extends Annotation>, Parser> parserMap = new HashMap<>();
    private final MetadataCache metadataCache = new MetadataCache();

	public ParserFacade(Dialect dialect, boolean escape) {
        this.dialect = dialect;
        parserMap.put(Entity.class, new EntityParser(dialect, escape));
        parserMap.put(Mapper.class, new MapperParser(dialect, escape));
	}

    @Override
    public MapperMetadata parse(Class<?> clazz) {
        var metadata = metadataCache.get(clazz);
        if (metadata != null) {
            logger.warn("{} 已经解析过", clazz.getName());
            return metadata;
        }

        for (var annotation : clazz.getAnnotations()) {
            if (!annotationSet.contains(annotation.annotationType())) {
                continue;
            }

            Parser parser = parserMap.get(annotation.annotationType());
            if (parser != null) {
                logger.debug("找到 {} 处理器: {}", annotation.annotationType().getSimpleName(), parser);
                var m = parser.parse(clazz);
                metadataCache.put(m);
                return m;
            }
        }
        return null;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public MetadataCache getMetadataCache() {
        return metadataCache;
    }
}
