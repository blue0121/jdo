package test.jutil.jdo.reflect;

import io.jutil.jdo.core.reflect.BeanField;
import io.jutil.jdo.core.reflect.JavaBean;
import io.jutil.jdo.core.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.CoreTest;
import test.jutil.jdo.model.TestModel;
import test.jutil.jdo.model.TestModel2;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class JavaBeanTest {
	public JavaBeanTest() {
	}

	@Test
	public void test1() {
		TestModel model = new TestModel();
		JavaBean bean = JavaBean.create(model, TestModel.class);
		Assertions.assertEquals(TestModel.class, bean.getTargetClass());
		Assertions.assertEquals(model, bean.getTarget());
		Assertions.assertNull(bean.getAnnotation(CoreTest.class));
		Map<String, BeanField> fieldMap = bean.getAllFields();
		Assertions.assertEquals(2, fieldMap.size());
		BeanField field1 = fieldMap.get("username");
		Assertions.assertNotNull(field1);
		Assertions.assertEquals("getUsername", field1.getGetterMethod().getName());
		Assertions.assertEquals("setUsername", field1.getSetterMethod().getName());
		Assertions.assertEquals("username", field1.getColumnName());
		Assertions.assertNotNull(field1.getSetterAnnotation(CoreTest.class));
		BeanField field2 = fieldMap.get("password");
		Assertions.assertNotNull(field2);
		Assertions.assertEquals("getPassword", field2.getGetterMethod().getName());
		Assertions.assertEquals("setPassword", field2.getSetterMethod().getName());
		Assertions.assertEquals("password", field2.getColumnName());
		Assertions.assertNotNull(field2.getSetterAnnotation(CoreTest.class));
		field1.setFieldValue(null, "name");
		field2.setFieldValue(null, "pass");
		Assertions.assertEquals("name", model.getUsername());
		Assertions.assertEquals("pass", model.getPassword());
		Assertions.assertEquals("name", field1.getFieldValue(null));
		Assertions.assertEquals("pass", field2.getFieldValue(null));

		var method1 = bean.getMethod(MethodSignature.create("getUsername"));
		Assertions.assertNotNull(method1);
		Assertions.assertEquals("getUsername", method1.getName());
		Assertions.assertTrue(method1.getParameterTypeList().isEmpty());
	}

	@Test
	public void test2() {
		TestModel2 model = new TestModel2();
		JavaBean bean = JavaBean.create(model, TestModel2.class);
		Assertions.assertEquals(TestModel2.class, bean.getTargetClass());
		Assertions.assertEquals(model, bean.getTarget());
		Assertions.assertNull(bean.getAnnotation(CoreTest.class));
		Map<String, BeanField> fieldMap = bean.getAllFields();
		Assertions.assertEquals(2, fieldMap.size());
		BeanField field1 = fieldMap.get("username");
		Assertions.assertNotNull(field1);
		Assertions.assertEquals("getUsername", field1.getGetterMethod().getName());
		Assertions.assertEquals("setUsername", field1.getSetterMethod().getName());
		Assertions.assertNotNull(field1.getAnnotation(CoreTest.class));
		BeanField field2 = fieldMap.get("password");
		Assertions.assertNotNull(field2);
		Assertions.assertEquals("getPassword", field2.getGetterMethod().getName());
		Assertions.assertEquals("setPassword", field2.getSetterMethod().getName());
		Assertions.assertNotNull(field2.getAnnotation(CoreTest.class));
		field1.setFieldValue(null, "name");
		field2.setFieldValue(null,"pass");
		Assertions.assertEquals("name", model.getUsername());
		Assertions.assertEquals("pass", model.getPassword());
		Assertions.assertEquals("name", field1.getFieldValue(null));
		Assertions.assertEquals("pass", field2.getFieldValue(null));
	}

	/*@Test
	public void testCache() {
		JavaBean bean1 = JavaBeanCache.getJavaBean(TestModel2.class);
		JavaBean bean2 = JavaBeanCache.getJavaBean(TestModel2.class);
		Assertions.assertTrue(bean1 == bean2);
	}*/

	@Test
	public void testNewInstance() {
		JavaBean bean = JavaBean.create(TestModel.class);
		Map<String, Object> map = Map.of("username", "blue", "password", 123456);
		TestModel model = (TestModel) bean.newInstanceQuietly(map);
		Assertions.assertNotNull(model);
		Assertions.assertEquals("blue", model.getUsername());
		Assertions.assertEquals("123456", model.getPassword());
	}

	@Test
	public void testGetAllFieldValues() {
		TestModel model = new TestModel();
		model.setUsername("blue");
		model.setPassword("password");
		JavaBean bean = JavaBean.create(model, TestModel.class);
		var map = bean.getAllFieldValues(null);
		Assertions.assertEquals(2, map.size());
		Assertions.assertEquals("blue", map.get("username"));
		Assertions.assertEquals("password", map.get("password"));

		bean = JavaBean.create(TestModel.class);
		map = bean.getAllFieldValues(null);
		Assertions.assertTrue(map.isEmpty());
	}

}
