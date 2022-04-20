package test.jutil.jdo.util;

import io.jutil.jdo.internal.core.util.MapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-20
 */
public class MapUtilTest {
	public MapUtilTest() {
	}

    @Test
    public void testMerge() {
        var origin = Map.of("k1", "v1", "k2", "v2");
		var dest = MapUtil.merge(origin, "k3", "v3");
	    Assertions.assertEquals(3, dest.size());
		Assertions.assertEquals("v1", dest.get("k1"));
	    Assertions.assertEquals("v2", dest.get("k2"));
	    Assertions.assertEquals("v3", dest.get("k3"));
    }
}
