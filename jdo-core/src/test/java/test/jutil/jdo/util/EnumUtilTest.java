package test.jutil.jdo.util;

import io.jutil.jdo.internal.core.util.EnumUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class EnumUtilTest {
	public EnumUtilTest() {
	}

	public enum Status {
		ACTIVE, INACTIVE
	}

	@Test
	public void testFromString() {
		Assertions.assertEquals(Status.ACTIVE, EnumUtil.fromString(Status.class, "ACTIVE"));
		Assertions.assertEquals(Status.INACTIVE, EnumUtil.fromString(Status.class, "INACTIVE"));
		Assertions.assertNull(EnumUtil.fromString(Status.class, null));
		Assertions.assertNull(EnumUtil.fromString(Status.class, ""));
		Assertions.assertNull(EnumUtil.fromString(Status.class, "ABC"));
		Assertions.assertNull(EnumUtil.fromString(EnumUtil.class, "ABC"));
	}

}
