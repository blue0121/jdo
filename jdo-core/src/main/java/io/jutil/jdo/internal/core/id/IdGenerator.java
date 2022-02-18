package io.jutil.jdo.internal.core.id;

/**
 * 不重复 ID 产生器
 *
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class IdGenerator {
	private IdGenerator() {
	}

	/**
	 * UUID－32位长度，有序
	 */
	public static String uuid32bit() {
		return UUID.generator();
	}

	/**
	 * UUID－12至13位长度，有序
	 */
	public static String uuid12bit() {
		return Long.toHexString(SnowflakeIdFactory.getSingleSnowflakeId().nextId());
	}

	/**
	 * long 类型，有序
	 */
	public static long id() {
		return SnowflakeIdFactory.getSingleSnowflakeId().nextId();
	}

}
