package test.jutil.jdo.id;

import io.jutil.jdo.core.collection.ConcurrentSet;
import io.jutil.jdo.internal.core.id.IdGenerator;
import io.jutil.jdo.internal.core.util.WaitUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public abstract class IdGeneratorTest<T> {
    protected IdGenerator<T> generator;

	public IdGeneratorTest() {
	}

    @Test
    public void testId() {
        for (int i = 0; i < 5; i++) {
            System.out.printf("ID值: %s\n", this.toString(generator.generate()));
        }
    }

    @Test
    public void testSingle() {
        int count = 1_000_000;
        ConcurrentSet<T> set = ConcurrentSet.create();
        var start = System.currentTimeMillis();
        for (int j = 0; j < count; j++) {
            this.addId(set);
        }
        var used = System.currentTimeMillis() - start;
        System.out.printf("用时: %d ms, 速度: %g/ms.\n", used, (double)set.size() / used);
        Assertions.assertEquals(count, set.size(), "单线程下ID有重复");
    }

    @Test
    public void testMulti() {
        int threads = 50;
        int count = 100_000;
        ConcurrentSet<T> set = ConcurrentSet.create();
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
        WaitUtil.await(latch);
        var used = System.currentTimeMillis() - start;
        System.out.printf("用时: %d ms, 速度: %g/ms.\n", used, (double)set.size() / used);
        Assertions.assertEquals(threads * count, set.size(), "多线程下ID有重复");
    }

    protected void addId(ConcurrentSet<T> set) {
        var id = generator.generate();
        if (!set.add(id)) {
            System.out.printf("重复ID: %s\n", this.toString(id));
        }
    }

    protected abstract String toString(T id);

}
