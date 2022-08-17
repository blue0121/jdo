package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.codec.Base32;
import io.jutil.jdo.internal.core.util.ByteUtil;
import io.jutil.jdo.internal.core.util.NetworkUtil;

/**
 * @author Jin Zheng
 * @since 2022-08-17
 */
public class String24IdGenerator extends AbstractIdGenerator<String> {
	private static final long SEQUENCE_BITS = 24;

    private final byte[] ip;

	public String24IdGenerator() {
        super(~(-1L << SEQUENCE_BITS));
		this.ip = NetworkUtil.getIpForByteArray();
	}

    @Override
    public synchronized String generate() {
	    var id = new byte[15];
	    this.generateTimestamp(id);
	    System.arraycopy(this.ip, 0, id, 11, 4);
	    return Base32.encode(id);
    }

	private synchronized void generateTimestamp(byte[] id) {
		this.generateSequence();
		ByteUtil.writeLong(id, 0, lastTimestamp);
		this.writeSequence(id, sequence);
	}

	private void writeSequence(byte[] id, long sequence) {
		var index = 8;
		id[index++] = (byte) ((sequence >> 16) & 0xff);
		id[index++] = (byte) ((sequence >> 8) & 0xff);
		id[index] = (byte) (sequence & 0xff);
	}
}
