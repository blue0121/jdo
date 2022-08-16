package test.jutil.jdo.codec;

import io.jutil.jdo.internal.core.codec.Base32;
import io.jutil.jdo.internal.core.codec.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2022-08-16
 */
public class Base32Test {
	public Base32Test() {
	}

	@Test
	public void testEncode() {
		String str = "hello world";
		var bytes = str.getBytes();

		System.out.println(Hex.encode(bytes));
		var base32 = Base32.encode(bytes);
		System.out.println(base32);
		Assertions.assertArrayEquals(bytes, Base32.decode(base32));
	}

}
