package io.jutil.jdo.internal.core.id;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class DefaultSnowflakeIdMetadata implements Metadata {
	private final LocalDateTime dateTime;
	private final long sequence;
	private final long machineId;

	public DefaultSnowflakeIdMetadata(long timestamp, long sequence, long machineId) {
		this.dateTime = Instant.ofEpochMilli(timestamp)
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		this.sequence = sequence;
		this.machineId = machineId;
	}

	public DefaultSnowflakeIdMetadata(LocalDateTime dateTime, long sequence, long machineId) {
		this.dateTime = dateTime;
		this.sequence = sequence;
		this.machineId = machineId;
	}

	@Override
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	@Override
	public long getSequence() {
		return sequence;
	}

	@Override
	public long getMachineId() {
		return machineId;
	}
}
