package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.util.ByteUtil;
import io.jutil.jdo.internal.core.util.NetworkUtil;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public class LongEpochIdGenerator extends AbstractEpochIdGenerator<Long> {
    private final long ipBits = 16;
    private final long sequenceBits = 12;
    private final long ipMask = ~(-1L << ipBits);
    private final long sequenceMask = ~(-1L << sequenceBits);
    private final long sequenceShift = ipBits;
    private final long timestampShift = ipBits + sequenceBits;

    private final long ip = this.getIp();

	public LongEpochIdGenerator() {
	}

    @Override
    public Long generate() {
        return null;
    }

    private long getIp() {
        var bytes = NetworkUtil.getIpForByteArray();
        if (bytes == null) {
            throw new IllegalArgumentException("无法获取IP");
        }
        var addr = ByteUtil.readInt(bytes, 0);
        return addr & ipMask;
    }
}
