package io.jutil.jdo.internal.core.id;

import java.time.LocalDateTime;

/**
 * 分布式发号器元数据
 *
 * @author Jin Zheng
 * @since 2022-02-18
 */
public interface Metadata
{
	/**
	 * 日期时间
	 * @return
	 */
	LocalDateTime getDateTime();

	/**
	 * 毫秒内的序列
	 * @return
	 */
	long getSequence();

	/**
	 * 机器ID
	 * @return
	 */
	long getMachineId();

}
