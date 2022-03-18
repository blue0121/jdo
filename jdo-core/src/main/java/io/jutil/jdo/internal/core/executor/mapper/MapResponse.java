package io.jutil.jdo.internal.core.executor.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-17
 */
public class MapResponse {
    private final Map<String, Object> fieldMap = new HashMap<>();

	public MapResponse() {
	}

    public void putField(String field, Object value) {
        fieldMap.put(field, value);
    }

    public void removeField(String field) {
        fieldMap.remove(field);
    }

	public Map<String, Object> toFieldMap() {
		return fieldMap;
	}

}
