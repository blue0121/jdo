package test.jutil.jdo.id;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public class EpochIdGeneratorTest {
	public EpochIdGeneratorTest() {
	}

    @Test
    public void testEpoch() {
        var now1 = LocalDateTime.now();
        var now2 = System.currentTimeMillis();
        var ts1 = now1.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        var ts2 = now1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println(ts1);
        System.out.println(ts2);
        System.out.println(now2);
        Assertions.assertTrue(Math.abs(now2 - ts1) < 100);
        Assertions.assertTrue(Math.abs(now2 - ts2) < 100);
    }

}
