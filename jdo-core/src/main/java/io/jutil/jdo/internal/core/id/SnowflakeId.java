package io.jutil.jdo.internal.core.id;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public interface SnowflakeId
{

	/**
	 * 产生ID
	 * @return
	 */
	long nextId();

	/**
	 * 根据解析SnowflakeId解析出元数据
	 *
	 * @param id SnowflakeId
	 * @return Id元数据
	 */
	Metadata getMetadata(long id);

}
