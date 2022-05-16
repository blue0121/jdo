package test.jutil.jdo.util;

import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.core.parser.IdMetadata;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.core.reflect.ClassFieldOperation;
import io.jutil.jdo.internal.core.executor.KeyHolder;
import io.jutil.jdo.internal.core.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

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
		var entityConfig = Mockito.mock(EntityMetadata.class);
		var idConfig = this.create("id1", IdType.INT, GeneratorType.INCREMENT);
		Mockito.when(entityConfig.getIdMetadata()).thenReturn(idConfig);
		IdUtil.setId(holder, target, entityConfig);
		Mockito.verify(idConfig.getFieldOperation()).setFieldValue(Mockito.eq(target), Mockito.eq(1));
	}

	@Test
	public void testSetIdLong() {
		var target = new Object();
		var holder = Mockito.mock(KeyHolder.class);
		Mockito.when(holder.getKey()).thenReturn(1L);
		var entityConfig = Mockito.mock(EntityMetadata.class);
		var idConfig = this.create("id1", IdType.LONG, GeneratorType.INCREMENT);
		Mockito.when(entityConfig.getIdMetadata()).thenReturn(idConfig);
		IdUtil.setId(holder, target, entityConfig);
		Mockito.verify(idConfig.getFieldOperation()).setFieldValue(Mockito.eq(target), Mockito.eq(1L));
	}

	@Test
	public void testSetIdListInt() {
		var target1 = new Object();
		var target2 = new Object();
		var holder = Mockito.mock(KeyHolder.class);
		Mockito.when(holder.getKeyList()).thenReturn(List.of(1, 2));
		var entityConfig = Mockito.mock(EntityMetadata.class);
		var idConfig = this.create("id1", IdType.INT, GeneratorType.INCREMENT);
		Mockito.when(entityConfig.getIdMetadata()).thenReturn(idConfig);
		IdUtil.setId(holder, List.of(target1, target2), entityConfig);
		Mockito.verify(idConfig.getFieldOperation()).setFieldValue(Mockito.eq(target1), Mockito.eq(1));
		Mockito.verify(idConfig.getFieldOperation()).setFieldValue(Mockito.eq(target2), Mockito.eq(2));
	}

	@Test
	public void testSetIdListLong() {
		var target1 = new Object();
		var target2 = new Object();
		var holder = Mockito.mock(KeyHolder.class);
		Mockito.when(holder.getKeyList()).thenReturn(List.of(1L, 2L));
		var entityConfig = Mockito.mock(EntityMetadata.class);
		var idConfig = this.create("id1", IdType.LONG, GeneratorType.INCREMENT);
		Mockito.when(entityConfig.getIdMetadata()).thenReturn(idConfig);
		IdUtil.setId(holder, List.of(target1, target2), entityConfig);
		Mockito.verify(idConfig.getFieldOperation()).setFieldValue(Mockito.eq(target1), Mockito.eq(1L));
		Mockito.verify(idConfig.getFieldOperation()).setFieldValue(Mockito.eq(target2), Mockito.eq(2L));
	}

	@Test
	public void testCheckSingleId() {
		IdMetadata id = this.create("id1", IdType.INT, GeneratorType.INCREMENT);
		var entityConfig = Mockito.mock(EntityMetadata.class);
		Mockito.when(entityConfig.getTargetClass()).then(i -> Object.class);
		Assertions.assertThrows(JdbcException.class, () -> IdUtil.checkSingleId(entityConfig));

		Mockito.when(entityConfig.getIdMetadata()).thenReturn(id);
		var id2 = IdUtil.checkSingleId(entityConfig);
		Assertions.assertEquals(id, id2);
	}

	private IdMetadata create(String fieldName, IdType idType, GeneratorType generatorType) {
		IdMetadata id = Mockito.spy(IdMetadata.class);
		Mockito.when(id.getFieldName()).thenReturn(fieldName);
		Mockito.when(id.getIdType()).thenReturn(idType);
		Mockito.when(id.getGeneratorType()).thenReturn(generatorType);
		Mockito.when(id.getFieldOperation()).thenReturn(Mockito.mock(ClassFieldOperation.class));
		return id;
	}

}


