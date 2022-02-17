package test.jutil.jdo.collection;

import io.jutil.jdo.core.collection.MultiMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class HashMultiMapTest {
	public HashMultiMapTest() {
	}

	@Test
	public void testHash() {
		MultiMap<Integer, Integer> map = MultiMap.create();
		map.put(1, 11);
		map.put(1, 12);
		Assertions.assertEquals(HashMap.class, map.getMapType());
		Assertions.assertEquals(1, map.size());
		Set<Integer> set = map.get(1);
		Assertions.assertNotNull(set);
		Assertions.assertEquals(2, set.size());
		Assertions.assertEquals(set, Set.of(11, 12));
		Assertions.assertTrue(map.remove(1));
		Assertions.assertTrue(map.get(1).isEmpty());
		Assertions.assertFalse(map.remove(1));
	}

	@Test
	public void testConcurrent() {
		MultiMap<Integer, Integer> map = MultiMap.createConcurrent();
		map.put(1, 1);
		map.put(2, 2);
		Assertions.assertEquals(ConcurrentHashMap.class, map.getMapType());
		Assertions.assertEquals(2, map.size());
		Assertions.assertEquals(1, map.getOne(1));
		Assertions.assertEquals(2, map.getOne(2));
	}

	@Test
	public void testLinked() {
		MultiMap<Integer, Integer> map = MultiMap.createLinked();
		map.put(1, 1);
		map.put(2, 2);
		Assertions.assertEquals(LinkedHashMap.class, map.getMapType());
		Assertions.assertEquals(2, map.size());
		Assertions.assertEquals(1, map.getOne(1));
		Assertions.assertEquals(2, map.getOne(2));
	}

	@Test
	public void testRemove() {
		MultiMap<Integer, Integer> map = MultiMap.create();
		map.put(1, 1);
		map.put(1, 2);
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(2, map.get(1).size());

		Assertions.assertFalse(map.remove(2, 1));
		Assertions.assertTrue(map.remove(1, 1));
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(1, map.get(1).size());

		Assertions.assertFalse(map.remove(1, 1));
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(1, map.get(1).size());

		Assertions.assertTrue(map.remove(1, 2));
		Assertions.assertEquals(0, map.size());
	}

}
