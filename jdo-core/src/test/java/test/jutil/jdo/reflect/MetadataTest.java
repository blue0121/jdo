package test.jutil.jdo.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-26
 */
public class MetadataTest {
	public MetadataTest() {
	}

	@Test
	public void testParse() {
		var annoClass = MetadataClass.class.getAnnotation(Metadata.class);
		Assertions.assertNotNull(annoClass);
		var mapClass = this.toMap(annoClass);
		this.verify(mapClass, "0", 0, 0L);
		int i = 1;
		for (var field : MetadataClass.class.getFields()) {
			var annoField = field.getAnnotation(Metadata.class);
			var mapField = this.toMap(annoField);
			this.verify(mapField, String.valueOf(i), i, i);
			i++;
		}
	}

	private Map<String, Object> toMap(Annotation anno) {
		var type = anno.annotationType();
		Map<String, Object> map = new HashMap<>();
		for (var method : type.getMethods()) {
			if (method.getParameterCount() > 0) {
				continue;
			}
			try {
				var value = method.invoke(anno);
				map.put(method.getName(), value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	private void verify(Map<String, Object> map, String value1, int value2, long value3) {
		Assertions.assertNotNull(map);
		System.out.println(map);
		Assertions.assertEquals(value1, map.get("strValue"));
		Assertions.assertEquals(value2, map.get("intValue"));
		Assertions.assertEquals(value3, map.get("longValue"));
	}

	@Metadata
	public static class MetadataClass {
		@Metadata(strValue = "1", intValue = 1, longValue = 1L)
		public String field1;
		@Metadata(strValue = "2", intValue = 2, longValue = 2L)
		public String field2;
	}


	@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Metadata {
		String strValue() default "0";

		int intValue() default 0;

		long longValue() default 0L;
	}

}
