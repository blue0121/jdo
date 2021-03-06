package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * UUID生成器
 *
 * @author Jin Zheng
 * @since 2022-02-18
 */
public final class UUID {
	private static Logger logger = LoggerFactory.getLogger(UUID.class);

	private static final String IP;
	private static short counter = (short) 0;
	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

	static {
		String ipadd = "00000000";
		try {
			ipadd = NumberUtil.toHexString(InetAddress.getLocalHost().getAddress());
		}
		catch (UnknownHostException e) {
			logger.warn("未知主机, ", e);
		}
		IP = ipadd;
	}

	private UUID() {
	}

	/**
	 * 产生并获取一个UUID
	 *
	 * @return UUID
	 */
	public static String generator() {
		StringBuilder sb = new StringBuilder(32);
		sb.append(IP);
		sb.append(NumberUtil.toHexString(JVM));
		sb.append(NumberUtil.toHexString((short) (System.currentTimeMillis() >>> 32)));
		sb.append(NumberUtil.toHexString((int) (System.currentTimeMillis())));
		sb.append(NumberUtil.toHexString(getCounter()));
		return sb.toString();
	}

	private static short getCounter() {
		synchronized (UUID.class) {
			if (counter < 0) {
				counter = 0;
			}
			return counter++;
		}
	}

}
