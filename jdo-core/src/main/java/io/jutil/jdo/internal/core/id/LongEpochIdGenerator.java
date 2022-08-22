package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.util.ByteUtil;
import io.jutil.jdo.internal.core.util.NetworkUtil;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public class LongEpochIdGenerator extends AbstractIdGenerator<Long> {
    private final EpochOptions options;
    private final long sequenceShift;
    private final long timestampShift;
    private final long ip;

    public LongEpochIdGenerator() {
        this(new EpochOptions());
    }

	public LongEpochIdGenerator(EpochOptions options) {
        super(options.getSequenceBits());
        this.options = options;
        this.sequenceShift = options.getIpBits();
        this.timestampShift = options.getIpBits() + options.getSequenceBits();
        this.ip = ((long)NetworkUtil.getIpForInt()) & ByteUtil.maskForInt(options.getIpBits());
	}

    @Override
    public synchronized Long generate() {
        this.generateSequence();

        return ((lastTimestamp - options.getEpochMillis()) << timestampShift)
                | (sequence << sequenceShift)
                | ip;
    }

}
