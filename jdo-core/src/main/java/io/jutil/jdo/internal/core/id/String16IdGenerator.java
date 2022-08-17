package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.codec.Base32;
import io.jutil.jdo.internal.core.util.NetworkUtil;

/**
 * @author Jin Zheng
 * @since 2022-08-15
 */
public class String16IdGenerator extends AbstractIdGenerator<String> {
    private static final long SEQUENCE_BITS = 12;
    private static final long IP_BITS = 24;

    private final EpochOptions options;
    private final long timestampShift;
    private final byte[] ip;

	public String16IdGenerator(EpochOptions options) {
        super(~(-1L << SEQUENCE_BITS));
        options.setIpBits(IP_BITS);
        options.setSequenceBits(SEQUENCE_BITS);
        this.options = options;
        this.timestampShift = options.getSequenceBits();
        this.ip = NetworkUtil.getIpForByteArray();
	}

    @Override
    public String generate() {
        var id = new byte[10];
        var timestamp = this.generateTimestamp();
        this.writeTimestamp(id, timestamp);
        System.arraycopy(this.ip, 1, id, 7, 3);
        return Base32.encode(id);
    }

    private synchronized long generateTimestamp() {
        this.generateSequence();

        return ((lastTimestamp - options.getEpochMillis()) << timestampShift)
                | sequence;
    }

    private void writeTimestamp(byte[] id, long timestamp) {
        var index = 0;
        id[index++] = (byte) ((timestamp >> 48) & 0xff);
        id[index++] = (byte) ((timestamp >> 40) & 0xff);
        id[index++] = (byte) ((timestamp >> 32) & 0xff);
        id[index++] = (byte) ((timestamp >> 24) & 0xff);
        id[index++] = (byte) ((timestamp >> 16) & 0xff);
        id[index++] = (byte) ((timestamp >> 8) & 0xff);
        id[index] = (byte) (timestamp & 0xff);
    }

}
