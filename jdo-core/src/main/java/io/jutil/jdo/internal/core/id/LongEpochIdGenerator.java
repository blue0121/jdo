package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.util.WaitUtil;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public class LongEpochIdGenerator implements IdGenerator<Long> {
    private final EpochOptions options;
    private final long sequenceShift;
    private final long timestampShift;

    private long sequence = 0L;
    private long lastTimestamp;

    public LongEpochIdGenerator() {
        this(new EpochOptions());
    }

	public LongEpochIdGenerator(EpochOptions options) {
        this.options = options;
        this.sequenceShift = options.getIpBits();
        this.timestampShift = options.getIpBits() + options.getSequenceBits();
	}

    @Override
    public synchronized Long generate() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new IllegalArgumentException(String.format("系统时钟回退，在 %d 毫秒内拒绝生成 ID",
                    lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & options.getSequenceMask();
            if (sequence == 0) {
                WaitUtil.sleep(1);
                lastTimestamp = System.currentTimeMillis();
            }
        } else {
            lastTimestamp = timestamp;
            sequence = 0L;
        }

        return ((lastTimestamp - options.getEpochMillis()) << timestampShift)
                | (sequence << sequenceShift)
                | (options.getIp());
    }

}
