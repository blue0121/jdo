package test.jutil.jdo.util;

import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.IdConfig;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.core.reflect.BeanField;
import io.jutil.jdo.internal.core.executor.KeyHolder;
import io.jutil.jdo.internal.core.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-04
 */
public class IdUtilTest {
	public IdUtilTest() {
	}

	@Test
	public void testSetIdInt() {
		var target = new Object();
		var holder = Mockito.mock(KeyHolder.class);
		Mockito.when(holder.getKey()).thenReturn(1);
		var entityConfig = Mockito.mock(EntityConfig.class);
		var idConfig = this.create("id1", IdType.INT, GeneratorType.INCREMENT);
		Mockito.when(entityConfig.getIdConfig()).thenReturn(idConfig);
		IdUtil.setId(holder, target, entityConfig);
		Mockito.verify(idConfig.getBeanField()).setFieldValue(Mockito.eq(target), Mockito.eq(1));
	}

	@Test
	public void testSetIdLong() {
		var target = new Object();
		var holder = Mockito.mock(KeyHolder.class);
		Mockito.when(holder.getKey()).thenReturn(1L);
		var entityConfig = Mockito.mock(EntityConfig.class);
		var idConfig = this.create("id1", IdType.LONG, GeneratorType.INCREMENT);
		Mockito.when(entityConfig.getIdConfig()).thenReturn(idConfig);
		IdUtil.setId(holder, target, entityConfig);
		Mockito.verify(idConfig.getBeanField()).setFieldValue(Mockito.eq(target), Mockito.eq(1L));
	}

	@Test
	public void testSetIdListInt() {
		var target1 = new Object();
		var target2 = new Object();
		var holder = Mockito.mock(KeyHolder.class);
		Mockito.when(holder.getKeyList()).thenReturn(List.of(1, 2));
		var entityConfig = Mockito.mock(EntityConfig.class);
		var idConfig = this.create("id1", IdType.INT, GeneratorType.INCREMENT);
		Mockito.when(entityConfig.getIdConfig()).thenReturn(idConfig);
		IdUtil.setId(holder, List.of(target1, target2), entityConfig);
		Mockito.verify(idConfig.getBeanField()).setFieldValue(Mockito.eq(target1), Mockito.eq(1));
		Mockito.verify(idConfig.getBeanField()).setFieldValue(Mockito.eq(target2), Mockito.eq(2));
	}

	@Test
	public void testSetIdListLong() {
		var target1 = new Object();
		var target2 = new Object();
		var holder = Mockito.mock(KeyHolder.class);
		Mockito.when(holder.getKeyList()).thenReturn(List.of(1L, 2L));
		var entityConfig = Mockito.mock(EntityConfig.class);
		var idConfig = this.create("id1", IdType.LONG, GeneratorType.INCREMENT);
		Mockito.when(entityConfig.getIdConfig()).thenReturn(idConfig);
		IdUtil.setId(holder, List.of(target1, target2), entityConfig);
		Mockito.verify(idConfig.getBeanField()).setFieldValue(Mockito.eq(target1), Mockito.eq(1L));
		Mockito.verify(idConfig.getBeanField()).setFieldValue(Mockito.eq(target2), Mockito.eq(2L));
	}

	@Test
	public void testGenerateId() {
		var idMap = this.createIdMap();
		Map<String, Object> map = new HashMap<>();
		IdUtil.generateId(map, idMap);
		Assertions.assertEquals(3, map.size());
		this.verify(map, idMap, false, false, false, false, false, false, true, true, true, false);
	}

	@Test
	public void testFilterId() {
		var idMap = this.createIdMap();
		Map<String, Object> map = new HashMap<>();
		for (var entry : idMap.entrySet()) {
			map.put(entry.getKey(), "uuid");
		}

		IdUtil.filterId(map, idMap);
		Assertions.assertEquals(6, map.size());
		this.verify(map, idMap, false, false, true, false, false, true, true, true, true, true);
	}

	@Test
	public void testCheckSingleId() {
		IdConfig id = this.create("id1", IdType.INT, GeneratorType.INCREMENT);
		var entityConfig = Mockito.mock(EntityConfig.class);
		Mockito.when(entityConfig.getClazz()).then(i -> Object.class);
		Assertions.assertThrows(JdbcException.class, () -> IdUtil.checkSingleId(entityConfig));

		Mockito.when(entityConfig.getIdConfig()).thenReturn(id);
		var id2 = IdUtil.checkSingleId(entityConfig);
		Assertions.assertEquals(id, id2);
	}

	private void verify(Map<String, Object> map, Map<String, IdConfig> idMap, boolean...exists) {
		int i = 0;
		for (var entry : idMap.entrySet()) {
			if (exists[i]) {
				Assertions.assertTrue(map.containsKey(entry.getKey()), entry.getKey());
			} else {
				Assertions.assertFalse(map.containsKey(entry.getKey()), entry.getKey());
			}
			i++;
		}
	}

	private Map<String, IdConfig> createIdMap() {
		IdConfig id11 = this.create("id11", IdType.INT, GeneratorType.INCREMENT);
		IdConfig id12 = this.create("id12", IdType.INT, GeneratorType.AUTO);
		IdConfig id13 = this.create("id13", IdType.INT, GeneratorType.ASSIGNED);
		IdConfig id21 = this.create("id21", IdType.LONG, GeneratorType.INCREMENT);
		IdConfig id22 = this.create("id22", IdType.LONG, GeneratorType.AUTO);
		IdConfig id23 = this.create("id23", IdType.LONG, GeneratorType.ASSIGNED);
		IdConfig id24 = this.create("id24", IdType.LONG, GeneratorType.UUID);
		IdConfig id31 = this.create("id31", IdType.STRING, GeneratorType.AUTO);
		IdConfig id32 = this.create("id32", IdType.STRING, GeneratorType.UUID);
		IdConfig id33 = this.create("id33", IdType.STRING, GeneratorType.ASSIGNED);

		Map<String, IdConfig> map = new LinkedHashMap<>();
		map.put(id11.getFieldName(), id11);
		map.put(id12.getFieldName(), id12);
		map.put(id13.getFieldName(), id13);
		map.put(id21.getFieldName(), id21);
		map.put(id22.getFieldName(), id22);
		map.put(id23.getFieldName(), id23);
		map.put(id24.getFieldName(), id24);
		map.put(id31.getFieldName(), id31);
		map.put(id32.getFieldName(), id32);
		map.put(id33.getFieldName(), id33);
		return map;
	}

	private Map<String, IdConfig> createIdMap(IdConfig... configs) {
		Map<String, IdConfig> map = new HashMap<>();
		for (var config : configs) {
			map.put(config.getFieldName(), config);
		}
		return map;
	}

	private IdConfig create(String fieldName, IdType idType, GeneratorType generatorType) {
		IdConfig id = Mockito.spy(IdConfig.class);
		Mockito.when(id.getFieldName()).thenReturn(fieldName);
		Mockito.when(id.getIdType()).thenReturn(idType);
		Mockito.when(id.getGeneratorType()).thenReturn(generatorType);
		Mockito.when(id.getBeanField()).thenReturn(Mockito.mock(BeanField.class));
		return id;
	}

}


