package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.util.WaitUtil;

/**
 * @author Jin Zheng
 * @since 2022-08-17
 */
public abstract class AbstractIdGenerator<T> implements IdGenerator<T> {
    protected final long sequenceMask;

    protected long sequence = 0L;
    protected long lastTimestamp = 0L;

    protected AbstractIdGenerator(long sequenceMask) {
        this.sequenceMask = sequenceMask;
    }

    protected void generateSequence() {
        long timestamp = System.currentTimeMillis();
        this.checkTimestamp(timestamp, lastTimestamp);

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                WaitUtil.sleep(1);
                lastTimestamp = System.currentTimeMillis();
            }
        } else {
            lastTimestamp = timestamp;
            sequence = 0L;
        }
    }

    private void checkTimestamp(long timestamp, long lastTimestamp) {
        if (timestamp >= lastTimestamp) {
            return;
        }

        var interval = timestamp - lastTimestamp;
        if (interval <= 50) {
            WaitUtil.sleep(interval);
        } else {
            throw new IllegalArgumentException(String.format("系统时钟回退，在 %d 毫秒内拒绝生成 ID", interval));
        }
    }

}
