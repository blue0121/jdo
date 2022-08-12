package test.jutil.jdo.util;

import io.jutil.jdo.internal.core.util.ByteUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author Jin Zheng
 * @since 2022-08-09
 */
public class ByteUtilTest {
	public ByteUtilTest() {
	}

    @CsvSource({"0xff, ff", "0x00, 00", "0xa0, a0", "0x0f, 0f", "0xf0, f0"})
    @ParameterizedTest
    public void testAppendHexString(int val, String hex) {
        byte b = (byte)(val & 0xff);
        StringBuilder builder = new StringBuilder();
        ByteUtil.appendHexString(builder, b);
        Assertions.assertEquals(hex, builder.toString());
    }

    @CsvSource({"ff, 0xff", "a0, 0xa0", "00, 0x00", "0f, 0x0f"})
    @ParameterizedTest
    public void testToByte(String hex, int val) {
        byte b = (byte)(val & 0xff);
        Assertions.assertEquals(b, ByteUtil.toByte(hex, 0));
    }

    @CsvSource({"0000ffff, 4", "00000000ffff0000, 8", "0, -1"})
    @ParameterizedTest
    public void testToBytes(String hex, int len) {
        if (len == -1) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ByteUtil.toBytes(hex), "无效16进制字符串");
            return;
        }

        var bytes = ByteUtil.toBytes(hex);
        Assertions.assertEquals(len, bytes.length);
    }

    @CsvSource({"0xff, 00ff", "0x0000, 0000"})
    @ParameterizedTest
    public void testToHexString(short val, String hex) {
        var bytes = new byte[2];
        ByteUtil.writeShort(bytes, 0, val);
        var str = ByteUtil.toHexString(bytes);
        Assertions.assertEquals(hex, str);

        var buf = ByteUtil.toBytes(str);
        var bufVal = ByteUtil.readShort(buf, 0);
        Assertions.assertEquals(val, bufVal);
    }

    @CsvSource({"0xffff, 0000ffff", "0x000000, 00000000"})
    @ParameterizedTest
    public void testToHexString(int val, String hex) {
        var bytes = new byte[4];
        ByteUtil.writeInt(bytes, 0, val);
        var str = ByteUtil.toHexString(bytes);
        Assertions.assertEquals(hex, str);

        var buf = ByteUtil.toBytes(str);
        var bufVal = ByteUtil.readInt(buf, 0);
        Assertions.assertEquals(val, bufVal);
    }

    @CsvSource({"0xffff0000, 00000000ffff0000", "0x000000, 0000000000000000"})
    @ParameterizedTest
    public void testToHexString(long val, String hex) {
        var bytes = new byte[8];
        ByteUtil.writeLong(bytes, 0, val);
        var str = ByteUtil.toHexString(bytes);
        Assertions.assertEquals(hex, str);

        var buf = ByteUtil.toBytes(str);
        var bufVal = ByteUtil.readLong(buf, 0);
        Assertions.assertEquals(val, bufVal);
    }

}
