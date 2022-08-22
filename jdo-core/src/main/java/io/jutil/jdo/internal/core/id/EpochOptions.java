package io.jutil.jdo.internal.core.id;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Jin Zheng
 * @since 2022-08-13
 */
public class EpochOptions {
	private LocalDate epoch;
    private int ipBits;
    private int sequenceBits;

    public EpochOptions() {
        this.epoch = LocalDate.of(2022, 1, 1);
        this.ipBits = 16;
        this.sequenceBits = 8;
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

    public int getIpBits() {
        return ipBits;
    }

    public EpochOptions setIpBits(int ipBits) {
        this.ipBits = ipBits;
        return this;
    }

    public int getSequenceBits() {
        return sequenceBits;
    }

    public EpochOptions setSequenceBits(int sequenceBits) {
        this.sequenceBits = sequenceBits;
        return this;
    }

}
