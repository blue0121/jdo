package test.jutil.jdo.id;

import io.jutil.jdo.core.collection.ConcurrentSet;
import io.jutil.jdo.internal.core.id.ByteArrayIdGenerator;
import io.jutil.jdo.internal.core.id.IdGenerator;
import io.jutil.jdo.internal.core.util.ByteUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author Jin Zheng
 * @since 2022-08-10
 */
public class ByteArrayIdGeneratorTest {
	private IdGenerator<byte[]> generator = new ByteArrayIdGenerator();

	public ByteArrayIdGeneratorTest() {
	}

	@Test
	public void testGenerate() {
		int count = 1_000_000;
		ConcurrentSet<String> set = ConcurrentSet.create();
		var start = System.currentTimeMillis();
		for (int j = 0; j < count; j++) {
			this.addId(set);
		}
		System.out.printf("用时: %d ms.\n", System.currentTimeMillis() - start);
		Assertions.assertEquals(count, set.size(), "单线程下UUID有重复");
	}

	private void addId(ConcurrentSet<String> set) {
		var b = generator.generate();
		var hex = ByteUtil.toHexString(b);
		if (!set.add(hex)) {
			System.out.printf("重复ID: %s\n", hex);
		}
	}

	@Test
	public void testGenerate2() throws InterruptedException {
		int threads = 50;
		int count = 100_000;
		ConcurrentSet<String> set = ConcurrentSet.create();
		var executor = Executors.newFixedThreadPool(threads);
		var latch = new CountDownLatch(threads);
		var start = System.currentTimeMillis();
		for (int i = 0; i < threads; i++) {
			executor.execute(() -> {
				try {
					for (int j = 0; j < count; j++) {
						this.addId(set);
					}
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();
		System.out.printf("用时: %d ms.\n", System.currentTimeMillis() - start);
		Assertions.assertEquals(threads * count, set.size(), "多线程下UUID有重复");
	}

	@Test
	public void testTimestamp() {
		var now = System.currentTimeMillis();
		var bytes = new byte[8];
		ByteUtil.writeLong(bytes, 0, now);
		String hex = ByteUtil.toHexString(bytes);
		System.out.println("当前时间: " + hex);
		Assertions.assertEquals(16, hex.length());

		var b = ByteUtil.toBytes("000009828c439d92");
		var instant = Instant.ofEpochMilli(ByteUtil.readLong(b, 0));
		System.out.println(instant);
	}

}
