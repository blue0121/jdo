package io.jutil.jdo.internal.core.id;

/**
 * 分步式发号器工厂
 *
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class SnowflakeIdFactory {
	private volatile static SnowflakeId singleSnowflakeId;

	private SnowflakeIdFactory() {
	}

	/**
	 * 创建分步式发号器
	 *
	 * @param machineId 机器ID
	 * @return
	 */
	public static SnowflakeId newSnowflakeId(long machineId) {
		return new DefaultSnowflakeId(machineId);
	}

	/**
	 * 单机发号器
	 *
	 * @return
	 */
	public static SnowflakeId getSingleSnowflakeId() {
		if (singleSnowflakeId == null) {
			synchronized (SnowflakeIdFactory.class) {
				if (singleSnowflakeId == null) {
					singleSnowflakeId = new DefaultSnowflakeId(0L, 12L, 0L);
				}
			}
		}
		return singleSnowflakeId;
	}

}
