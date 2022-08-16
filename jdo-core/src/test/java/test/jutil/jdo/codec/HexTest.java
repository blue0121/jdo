package test.jutil.jdo.codec;

import io.jutil.jdo.internal.core.codec.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author Jin Zheng
 * @since 2022-08-16
 */
public class HexTest {
	public HexTest() {
	}

    @CsvSource({"0xff, ff", "0x00, 00", "0xa0, a0", "0x0f, 0f", "0xf0, f0"})
    @ParameterizedTest
    public void testEncode(int val, String hex) {
        byte b = (byte)(val & 0xff);
        Assertions.assertEquals(hex, Hex.encode(new byte[] {b}));
    }

    @CsvSource({"ff, 0xff", "a0, 0xa0", "00, 0x00", "0f, 0x0f"})
    @ParameterizedTest
    public void testDecode(String hex, int val) {
        byte b = (byte)(val & 0xff);
        Assertions.assertEquals(b, Hex.decode(hex)[0]);
    }

    @CsvSource({"0000ffff, 4", "00000000ffff0000, 8", "0, -1"})
    @ParameterizedTest
    public void testDecode2(String hex, int len) {
        if (len == -1) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Hex.decode(hex), "无效16进制字符串");
            return;
        }

        var bytes = Hex.decode(hex);
        Assertions.assertEquals(len, bytes.length);
    }

}
