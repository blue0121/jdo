package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.codec.Base32;
import io.jutil.jdo.internal.core.util.NetworkUtil;

/**
 * @author Jin Zheng
 * @since 2022-08-15
 */
public class String20IdGenerator extends AbstractIdGenerator<String> {
    private static final int SEQUENCE_BITS = 12;

    private final EpochOptions options;
    private final long timestampShift;
    private final byte[] ip;

	public String20IdGenerator(EpochOptions options) {
        super(SEQUENCE_BITS);
        this.options = options;
        this.timestampShift = SEQUENCE_BITS;
        this.ip = NetworkUtil.getIpForByteArray();
	}

    @Override
    public String generate() {
        var id = new byte[12];
        var timestamp = this.generateTimestamp();
        this.writeTimestamp(id, timestamp, 8);
        System.arraycopy(this.ip, 0, id, 8, 3);
        return Base32.encode(id);
    }

    private synchronized long generateTimestamp() {
        this.generateSequence();

        return ((lastTimestamp - options.getEpochMillis()) << timestampShift)
                | sequence;
    }

}
