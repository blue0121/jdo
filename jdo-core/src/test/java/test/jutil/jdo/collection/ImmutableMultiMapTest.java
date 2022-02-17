package test.jutil.jdo.collection;

import io.jutil.jdo.core.collection.MultiMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class ImmutableMultiMapTest {
	public ImmutableMultiMapTest() {
	}

	@Test
	public void test1() {
		MultiMap<Integer, Integer> map = MultiMap.create();
		map.put(1, 11);
		map.put(1, 12);
		MultiMap<Integer, Integer> readOnlyMap = MultiMap.copyOf(map);
		Assertions.assertEquals(HashMap.class, readOnlyMap.getMapType());
		Assertions.assertEquals(1, readOnlyMap.size());
		Set<Integer> set = readOnlyMap.get(1);
		Assertions.assertNotNull(set);
		Assertions.assertEquals(2, set.size());
		Assertions.assertEquals(set, Set.of(11, 12));
		Assertions.assertThrows(UnsupportedOperationException.class, () -> readOnlyMap.clear());
		Assertions.assertThrows(UnsupportedOperationException.class, () -> readOnlyMap.put(2, 2));
		Assertions.assertThrows(UnsupportedOperationException.class, () -> readOnlyMap.remove(1));
	}

}
