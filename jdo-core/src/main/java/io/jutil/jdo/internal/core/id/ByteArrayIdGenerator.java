package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.util.ByteUtil;
import io.jutil.jdo.internal.core.util.WaitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Jin Zheng
 * @since 2022-05-26
 */
public class ByteArrayIdGenerator implements IdGenerator<byte[]> {
	private static Logger logger = LoggerFactory.getLogger(ByteArrayIdGenerator.class);

	private final byte[] ip;

	private long sequenceMask = 0xffff;
	private long sequence = 0L;
	private long lastTimestamp;

	public ByteArrayIdGenerator() {
		this.ip = this.getIp();
	}

	@Override
	public byte[] generate() {
		var id = new byte[16];

		this.getSequence(id);

		ByteUtil.writeBytes(id, 12, ip);

		return id;
	}

	private synchronized void getSequence(byte[] id) {
		long timestamp = System.currentTimeMillis();
		if (timestamp < lastTimestamp) {
			throw new IllegalArgumentException(String.format("系统时钟回退，在 %d 毫秒内拒绝生成 ID", lastTimestamp - timestamp));
		}

		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				WaitUtil.sleep(1);
				lastTimestamp = System.currentTimeMillis();
			}
		} else {
			lastTimestamp = timestamp;
			sequence = 0L;
		}

		ByteUtil.writeLong(id, 0, lastTimestamp);
		ByteUtil.writeInt(id, 8, (int)sequence);
	}

	private byte[] getIp() {
		try {
			return InetAddress.getLocalHost().getAddress();
		} catch (UnknownHostException e) {
			logger.warn("未知主机, ", e);
			return new byte[4];
		}
	}
}
