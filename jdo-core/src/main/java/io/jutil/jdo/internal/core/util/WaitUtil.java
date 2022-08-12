package io.jutil.jdo.internal.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            logger.warn("Wakeup, ", e);
        }
    }

}
