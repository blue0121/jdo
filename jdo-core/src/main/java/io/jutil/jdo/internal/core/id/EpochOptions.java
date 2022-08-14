package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.util.ByteUtil;
import io.jutil.jdo.internal.core.util.NetworkUtil;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Jin Zheng
 * @since 2022-08-13
 */
public class EpochOptions {
	private LocalDate epoch;
    private long ipBits;
    private long sequenceBits;
    private long ip;
    private long sequenceMask;

    public EpochOptions() {
        this.epoch = LocalDate.of(2022, 1, 1);
        this.ipBits = 16;
        this.sequenceBits = 8;
        this.sequenceMask = ~(-1L << sequenceBits);
        this.ip = generateIp() & (~(-1L << ipBits));
    }

    public long getSequenceMask() {
        return sequenceMask;
    }

    public long getIp() {
        return ip;
    }

    public long getEpochMillis() {
        return epoch.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public LocalDate getEpoch() {
        return epoch;
    }

    public EpochOptions setEpoch(LocalDate epoch) {
        this.epoch = epoch;
        return this;
    }

    public long getIpBits() {
        return ipBits;
    }

    public EpochOptions setIpBits(long ipBits) {
        this.ipBits = ipBits;
        this.ip = generateIp() & (~(-1L << ipBits));
        return this;
    }

    public long getSequenceBits() {
        return sequenceBits;
    }

    public EpochOptions setSequenceBits(long sequenceBits) {
        this.sequenceBits = sequenceBits;
        this.sequenceMask = ~(-1L << sequenceBits);
        return this;
    }

    public static long generateIp() {
        var bytes = NetworkUtil.getIpForByteArray();
        if (bytes == null) {
            throw new IllegalArgumentException("无法获取IP");
        }
        return ByteUtil.readInt(bytes, 0);
    }
}
