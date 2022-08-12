package test.jutil.jdo.util;

import io.jutil.jdo.internal.core.util.ByteUtil;
import io.jutil.jdo.internal.core.util.NetworkUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public class NetworkUtilTest {
	public NetworkUtilTest() {
	}

    @Test
    public void testGetIpList() {
        var list = NetworkUtil.getIpList();
        Assertions.assertFalse(list.isEmpty());
        System.out.println(list);
    }

	@Test
	public void testGetIpForByteArray() {
		var ip = NetworkUtil.getIpForByteArray();
		Assertions.assertNotNull(ip);
		Assertions.assertEquals(4, ip.length);
		System.out.println(Arrays.toString(ip));
		var id = ByteUtil.readInt(ip, 0);
		System.out.println(id);
	}

	@Test
	public void testGetIpForString() {
		var ip = NetworkUtil.getIpForString();
		Assertions.assertNotNull(ip);
		System.out.println(ip);
	}

}
