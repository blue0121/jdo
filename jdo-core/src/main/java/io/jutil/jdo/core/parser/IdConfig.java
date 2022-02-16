package io.jutil.jdo.core.parser;


import io.jutil.jdo.core.annotation.GeneratorType;

/**
 * 主键配置
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
public interface IdConfig extends FieldConfig {

	/**
	 * 主键类型
	 */
	IdType getIdType();

	/**
	 * 主键生成类型
	 */
	GeneratorType getGeneratorType();

	/**
	 * 是否数据库生成
	 * @return
	 */
	default boolean isDbGenerated() {
		var type = this.getGeneratorType();
		if (type == GeneratorType.INCREMENT) {
			return true;
		}
		var idType = this.getIdType();
		var isNum = idType == IdType.INT || idType == IdType.LONG;
		if (type == GeneratorType.AUTO && isNum) {
			return true;
		}
		return false;
	}

}
