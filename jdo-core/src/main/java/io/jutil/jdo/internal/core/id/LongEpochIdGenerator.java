package io.jutil.jdo.internal.core.id;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public class LongEpochIdGenerator extends AbstractIdGenerator<Long> {
    private final EpochOptions options;
    private final long sequenceShift;
    private final long timestampShift;

    public LongEpochIdGenerator() {
        this(new EpochOptions());
    }

	public LongEpochIdGenerator(EpochOptions options) {
        super(options.getSequenceMask());
        this.options = options;
        this.sequenceShift = options.getIpBits();
        this.timestampShift = options.getIpBits() + options.getSequenceBits();
	}

    @Override
    public synchronized Long generate() {
        this.generateSequence();

        return ((lastTimestamp - options.getEpochMillis()) << timestampShift)
                | (sequence << sequenceShift)
                | (options.getIp());
    }

}
