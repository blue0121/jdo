package test.jutil.jdo.reflect;

import io.jutil.jdo.core.reflect.ReflectFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

/**
 * @author Jin Zheng
 * @since 2022-05-05
 */
public class ClassOperationTest {
	public ClassOperationTest() {
	}

	@ParameterizedTest
	@CsvSource({"test.jutil.jdo.reflect.ClassOperationTest$CommonClass,false",
			"test.jutil.jdo.reflect.ClassOperationTest$CommonClass2,true"})
	public void testParse(Class<?> targetClass, boolean isPublic) {
		var clazz = ReflectFactory.getClassOperation(targetClass);
		Assertions.assertEquals(targetClass, clazz.getTargetClass());
		Assertions.assertNull(clazz.getAnnotation(Common.class));
		Assertions.assertTrue(clazz.getAnnotations().isEmpty());

		var fieldMap = clazz.getAllFields();
		Assertions.assertEquals(2, fieldMap.size());
		var field1 = fieldMap.get("username");
		Assertions.assertNotNull(field1);
		Assertions.assertEquals(isPublic, Modifier.isPublic(field1.getModifiers()));
		if (!isPublic) {
			Assertions.assertEquals("getUsername", field1.getGetterMethod().getName());
			Assertions.assertEquals("setUsername", field1.getSetterMethod().getName());
		}
		Assertions.assertNotNull(field1.getAnnotation(Common.class));

		var field2 = fieldMap.get("password");
		Assertions.assertNotNull(field2);
		Assertions.assertEquals(isPublic, Modifier.isPublic(field2.getModifiers()));
		if (!isPublic) {
			Assertions.assertEquals("getPassword", field2.getGetterMethod().getName());
			Assertions.assertEquals("setPassword", field2.getSetterMethod().getName());
		}
		Assertions.assertNotNull(field2.getAnnotation(Common.class));
	}

	@ParameterizedTest
	@CsvSource({"test.jutil.jdo.reflect.ClassOperationTest$CommonClass",
			"test.jutil.jdo.reflect.ClassOperationTest$CommonClass2"})
	public void testValue(Class<?> targetClass) {
		String username = "username";
		String password = "password";
		var clazz = ReflectFactory.getClassOperation(targetClass);
		Object target = switch (targetClass.getSimpleName()) {
			case "CommonClass" -> new CommonClass(username, password);
			case "CommonClass2" -> new CommonClass2(username, password);
			default -> new UnsupportedOperationException("不支持类型: " + targetClass.getName());
		};
		var valueMap = clazz.getAllFieldValues(target);
		Assertions.assertEquals(username, valueMap.get(username));
		Assertions.assertEquals(password, valueMap.get(password));
	}

	@Test
	public void testCache() {
		var class1 = ReflectFactory.getClassOperation(CommonClass.class);
		var class2 = ReflectFactory.getClassOperation(CommonClass.class);
		Assertions.assertSame(class1, class2);
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public class CommonClass {
		@Common
		private String username;
		@Common
		private String password;
	}

	@AllArgsConstructor
	public class CommonClass2 {
		@Common
		public String username;
		@Common
		public String password;
	}

	@Target({ElementType.FIELD, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Common {
		String value() default "";
	}

}
