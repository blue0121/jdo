package test.jutil.jdo.reflect;

import io.jutil.jdo.internal.core.reflect.RefelctConst;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-05-01
 */
public class MetadataTest {
	public MetadataTest() {
	}

	@Test
	public void testParse() {
		var annotations = MetadataClass.class.getAnnotations();
		Map<Class<?>, Annotation> annotationMap = new HashMap<>();
		for (var annotation : annotations) {
			System.out.println(annotation.annotationType() + ": " + annotation.getClass());
			Assertions.assertTrue(Proxy.isProxyClass(annotation.getClass()));
			this.parseAnnotation(annotationMap, annotation);
			annotationMap.put(annotation.annotationType(), annotation);
		}
		Assertions.assertEquals(2, annotationMap.size());
		var annotationMetadata = (Metadata)annotationMap.get(Metadata.class);
		Assertions.assertNotNull(annotationMetadata);
		Assertions.assertEquals("1", annotationMetadata.strValue());
		Assertions.assertEquals(1, annotationMetadata.intValue());
		Assertions.assertEquals(1L, annotationMetadata.longValue());
	}

	private void parseAnnotation(Map<Class<?>, Annotation> annotationMap, Annotation annotation) {
		var superAnnotations = annotation.annotationType().getAnnotations();
		for (var superAnnotation : superAnnotations) {
			if (superAnnotation.annotationType().getName().startsWith("java")) {
				continue;
			}
			var newSuperAnnotation = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
					new Class[] {superAnnotation.annotationType()},
					new AnnotationInvocationHandler(annotation, superAnnotation));
			annotationMap.put(superAnnotation.annotationType(), (Annotation) newSuperAnnotation);
		}
	}

	public static class AnnotationInvocationHandler implements InvocationHandler {
		private final Annotation superAnnotation;
		private final Map<String, Object> annotationMap;

		public AnnotationInvocationHandler(Annotation annotation, Annotation superAnnotation) {
			this.superAnnotation = superAnnotation;
			this.annotationMap = toMap(annotation);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			var methodName = method.getName();
			if (RefelctConst.IGNORE_METHOD_SET.contains(methodName)
					|| !annotationMap.containsKey(methodName)) {
				return method.invoke(superAnnotation, args);
			}
			var value = annotationMap.get(methodName);

			return value;
		}
	}

	private static Map<String, Object> toMap(Annotation annotation) {
		var type = annotation.annotationType();
		Map<String, Object> map = new HashMap<>();
		for (var method : type.getMethods()) {
			var methodName = method.getName();
			if (RefelctConst.IGNORE_METHOD_SET.contains(methodName)) {
				continue;
			}
			try {
				var value = method.invoke(annotation);
				map.put(methodName, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	@Metadata1(longValue = 1L)
	public static class MetadataClass {
	}

	@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Metadata(strValue = "1", intValue = 1)
	public @interface Metadata1 {
		long longValue();
	}

	@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Metadata {
		String strValue() default "0";

		int intValue() default 0;

		long longValue() default 0L;
	}
}
