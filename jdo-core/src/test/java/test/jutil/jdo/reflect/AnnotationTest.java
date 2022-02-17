package test.jutil.jdo.reflect;

import io.jutil.jdo.core.reflect.BeanConstructor;
import io.jutil.jdo.core.reflect.BeanMethod;
import io.jutil.jdo.core.reflect.JavaBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.Cat;
import test.jutil.jdo.model.MoveAction;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class AnnotationTest {
	public AnnotationTest() {
	}

	@Test
	public void test1() {
		Cat cat = new Cat();
		JavaBean catBean = JavaBean.create(cat, cat.getClass());
		Assertions.assertNotNull(catBean.getAnnotation(MoveAction.class));
		Assertions.assertNull(catBean.getDeclaredAnnotation(MoveAction.class));
		Assertions.assertEquals(1, catBean.getAnnotations().size());
		Assertions.assertEquals(0, catBean.getDeclaredAnnotations().size());

	}

	@Test
	public void test2() {
		JavaBean catBean = JavaBean.create(Cat.class);
		List<BeanMethod> methodList = catBean.getAllMethods();
		for (BeanMethod beanMethod : methodList) {
			System.out.println(beanMethod.getName());
		}
		Assertions.assertEquals(1, methodList.size());
		BeanMethod method = methodList.get(0);
		Assertions.assertEquals("move", method.getName());
		Assertions.assertNull(method.getRepresentField());
		Assertions.assertFalse(method.isGetter());
		Assertions.assertFalse(method.isSetter());
		Assertions.assertNull(method.getDeclaredAnnotation(MoveAction.class));
		Assertions.assertNotNull(method.getAnnotation(MoveAction.class));
	}

	@Test
	public void test3() {
		JavaBean catBean = JavaBean.create(Cat.class);
		List<BeanConstructor> constructorList = catBean.getAllConstructors();
		Assertions.assertEquals(1, constructorList.size());
		Assertions.assertNull(catBean.getConstructor(Integer.class));

		Cat cat = (Cat) catBean.newInstanceQuietly();
		Assertions.assertNotNull(cat);
	}

}
