package test.jutil.jdo.collection;

import io.jutil.jdo.core.collection.ConcurrentSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class ConcurrentHashSetTest {
	public ConcurrentHashSetTest() {
	}

	@Test
	public void test1() {
		Set<Integer> set = ConcurrentSet.create(null);
		set.add(1);
		set.add(2);
		Assertions.assertEquals(2, set.size());
		set.add(2);
		Assertions.assertEquals(2, set.size());
	}

}
