package io.jutil.jdo.internal.core.reflect;

import io.jutil.jdo.core.reflect.AnnotationOperation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-29
 */
public class DefaultAnnotationOperation implements AnnotationOperation {
    private final AnnotatedElement annotatedElement;
    private Map<Class<?>, Annotation> annotationMap;
    private List<Annotation> annotationList;

    protected DefaultAnnotationOperation(AnnotatedElement annotatedElement) {
        this.annotatedElement = annotatedElement;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return (T) annotationMap.get(annotationClass);
    }

    @Override
    public List<Annotation> getAnnotations() {
        return annotationList;
    }

    /**
     * 初始化注解Map
     *
     * @param annotationMap
     */
    protected final void initAnnotationMap(Map<Class<?>, Annotation> annotationMap) {
        this.annotationMap = Map.copyOf(annotationMap);
        this.annotationList = List.copyOf(annotationMap.values());
    }
}
