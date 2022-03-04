package test.jutil.jdo.util;

import io.jutil.jdo.core.parser.EntityConfig;
import io.jutil.jdo.core.parser.VersionConfig;
import io.jutil.jdo.internal.core.util.VersionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-03-04
 */
public class VersionUtilTest {
	public VersionUtilTest() {
	}

	@Test
	public void testGenerate() {
		Map<String, Object> map = new HashMap<>();
		var entity1 = this.create("id1", true, 1);
		VersionUtil.generate(map, entity1.getVersionConfig());
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(1, map.get("id1"));

		var entity2 = this.create("id1", true, 2);
		VersionUtil.generate(map, entity2.getVersionConfig());
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(1, map.get("id1"));
	}

	@Test
	public void testIsForce() {
		var entity1 = this.create("id1", true, 1);
		Assertions.assertTrue(VersionUtil.isForce(entity1));

		var entity2 = this.create("id1", false, 1);
		Assertions.assertFalse(VersionUtil.isForce(entity2));

		var entity3 = Mockito.mock(EntityConfig.class);
		Assertions.assertFalse(VersionUtil.isForce(entity3));
	}

	private EntityConfig create(String fieldName, boolean isForce, int defaultValue) {
		var entity = Mockito.mock(EntityConfig.class);
		var ver = Mockito.mock(VersionConfig.class);
		Mockito.when(entity.getVersionConfig()).thenReturn(ver);
		Mockito.when(ver.getFieldName()).thenReturn(fieldName);
		Mockito.when(ver.isForce()).thenReturn(isForce);
		Mockito.when(ver.getDefaultValue()).thenReturn(defaultValue);
		return entity;
	}

}
