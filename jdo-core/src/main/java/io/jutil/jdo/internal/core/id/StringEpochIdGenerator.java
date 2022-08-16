package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.codec.Base32;
import io.jutil.jdo.internal.core.util.NetworkUtil;
import io.jutil.jdo.internal.core.util.WaitUtil;

/**
 * @author Jin Zheng
 * @since 2022-08-15
 */
public class StringEpochIdGenerator implements IdGenerator<String> {
    private static final long TIMESTAMP_BITS = 44;
    private static final long SEQUENCE_BITS = 12;
    private static final long IP_BITS = 24;

    private final EpochOptions options;
    private final long timestampShift;
    private final byte[] ip;

    private long sequence = 0L;
    private long lastTimestamp;

	public StringEpochIdGenerator(EpochOptions options) {
        options.setIpBits(IP_BITS);
        options.setSequenceBits(SEQUENCE_BITS);
        this.options = options;
        this.timestampShift = options.getSequenceBits();
        this.ip = NetworkUtil.getIpForByteArray();
        if (ip == null) {
            throw new IllegalArgumentException("无法获取IP");
        }
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
