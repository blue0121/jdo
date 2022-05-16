package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.core.parser.MapperMetadata;
import io.jutil.jdo.core.parser.MetadataType;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-05-12
 */
@NoArgsConstructor
public class MetadataCache {
    private final Map<Class<?>, MapperMetadata> metadataMap = new HashMap<>();

    public void put(MapperMetadata metadata) {
        metadataMap.put(metadata.getTargetClass(), metadata);
    }

    public boolean exist(Class<?> clazz) {
        return metadataMap.containsKey(clazz);
    }

    public boolean exist(Class<?> clazz, MetadataType type) {
        var config = metadataMap.get(clazz);
        if (config != null) {
            return config.getMetadataType() == type;
        }
        return false;
    }

    public MapperMetadata get(Class<?> clazz) {
        return metadataMap.get(clazz);
    }

    public EntityMetadata getEntityMetadata(Class<?> clazz) {
        var config = metadataMap.get(clazz);
        if (config instanceof EntityMetadata c) {
            return c;
        }
        return null;
    }

    public MapperMetadata load(Class<?> clazz) {
        var config = metadataMap.get(clazz);
        if (config == null) {
            throw new JdbcException("找不到配置: " + clazz.getName());
        }
        return config;
    }

    public EntityMetadata loadEntityMetadata(Class<?> clazz) {
        var config = this.getEntityMetadata(clazz);
        if (config == null) {
            throw new JdbcException("找不到实体配置: " + clazz.getName());
        }
        return config;
    }

    public Map<Class<?>, MapperMetadata> all() {
        return Map.copyOf(metadataMap);
    }

    public Map<Class<?>, EntityMetadata> allEntityMetadata() {
        Map<Class<?>, EntityMetadata> map = new HashMap<>();
        for (var entry : metadataMap.entrySet()) {
            if (entry.getValue() instanceof EntityMetadata c) {
                map.put(entry.getKey(), c);
            }
        }
        return map;
    }

}
