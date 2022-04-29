package io.jutil.jdo.internal.core.reflect2;

import io.jutil.jdo.core.reflect2.AnnotationMetadata;
import io.jutil.jdo.core.reflect2.AnnotationOperation;

import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-29
 */
public class DefaultAnnotationOperation implements AnnotationOperation {
    private Map<Class<?>, AnnotationMetadata> annotationMap;
    private List<AnnotationMetadata> annotationList;

    public DefaultAnnotationOperation() {
    }

    @Override
    public AnnotationMetadata getAnnotation(Class<?> annotationClass) {
        return null;
    }

    @Override
    public List<AnnotationMetadata> getAnnotations() {
        return null;
    }

    protected final void initAnnotationMap(Map<Class<?>, AnnotationMetadata> annotationMap) {
        this.annotationMap = Map.copyOf(annotationMap);
        this.annotationList = List.copyOf(annotationMap.values());
    }
}
