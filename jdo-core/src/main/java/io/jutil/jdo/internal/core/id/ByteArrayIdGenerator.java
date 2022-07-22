package io.jutil.jdo.internal.core.id;

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

    private final long sequenceMask = 0xffff;
    private final byte[] ip;
    private final int jvm;

    private long lastTimestamp;
    private short sequence;

	public ByteArrayIdGenerator() {
        this.ip = this.getIp();
        this.jvm = (int) (System.currentTimeMillis() >>> 8);
    }

    @Override
    public byte[] generate() {
        var id = new byte[16];
        System.arraycopy(ip, 0, id, 0, ip.length);

        var now = System.currentTimeMillis();
        short high = (short) (now >>> 32);
        int low = (int) now;
        short seq = this.getSequence();

        this.writeShort(id, 0, high);
        this.writeInt(id, 2, low);
        this.writeInt(id, 6, jvm);
        this.writeShort(id, 10, seq);

        return id;
    }

    private void writeShort(byte[] buffer, int startIndex, short val) {
        buffer[startIndex++] = (byte) ((val >> 8) & 0xff);
        buffer[startIndex++] = (byte) (val & 0xff);
    }

    private void writeInt(byte[] buffer, int startIndex, int val) {
        buffer[startIndex++] = (byte) ((val >> 24) & 0xff);
        buffer[startIndex++] = (byte) ((val >> 16) & 0xff);
        buffer[startIndex++] = (byte) ((val >> 8) & 0xff);
        buffer[startIndex++] = (byte) (val & 0xff);
    }

    private synchronized short getSequence() {
        if (sequence < 0) {
            sequence = 0;
        }
        return sequence++;
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
