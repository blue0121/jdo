package io.jutil.jdo.core.parser;

import java.util.Map;

/**
 * 实体元数据
 *
 * @author Jin Zheng
 * @since 2022-05-11
 */
public interface EntityMetadata extends MapperMetadata {

	/**
	 * 表名
	 *
	 * @return
	 */
	String getTableName();

	/**
	 * 单个主键元数据
	 *
	 * @return
	 */
	IdMetadata getIdMetadata();

	/**
	 * 所有主键元数据
	 *
	 * @return Map<字段名, 主键元数据>
	 */
	Map<String, IdMetadata> getIdMap();

	/**
	 * 版本元数据
	 *
	 * @return
	 */
	VersionMetadata getVersionMetadata();

	/**
	 * 额外字段元数据
	 *
	 * @return Map<字段名, 额外字段元数据>
	 */
	Map<String, TransientMetadata> getTransientMap();

	/**
	 * 自动生成的SQL元数据
	 *
	 * @return
	 */
	SqlMetadata getSqlMetadata();

}
