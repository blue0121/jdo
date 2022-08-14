package io.jutil.jdo.internal.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Jin Zheng
 * @since 2022-08-10
 */
public class WaitUtil {
    private static Logger logger = LoggerFactory.getLogger(WaitUtil.class);

	private WaitUtil() {
	}

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.warn("Interrupted, ", e);
        }
    }

	public static void await(CountDownLatch latch) {
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.warn("Interrupted, ", e);
		}
	}

}
