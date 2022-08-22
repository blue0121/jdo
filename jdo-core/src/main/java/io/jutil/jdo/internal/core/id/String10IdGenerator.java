package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.codec.Base32;

/**
 * @author Jin Zheng
 * @since 2022-08-22
 */
public class String10IdGenerator extends AbstractIdGenerator<String> {
    private static final int SEQUENCE_BITS = 12;

    private final EpochOptions options;
    private final long timestampShift;

	public String10IdGenerator(EpochOptions options) {
        super(SEQUENCE_BITS);
        this.options = options;
        this.timestampShift = SEQUENCE_BITS;
	}

    @Override
    public String generate() {
        var id = new byte[6];
        var timestamp = this.generateTimestamp();
        this.writeTimestamp(id, timestamp, 6);
        return Base32.encode(id);
    }

    private synchronized long generateTimestamp() {
        this.generateSequence();

        return ((lastTimestamp - options.getEpochMillis()) << timestampShift)
                | sequence;
    }

}
