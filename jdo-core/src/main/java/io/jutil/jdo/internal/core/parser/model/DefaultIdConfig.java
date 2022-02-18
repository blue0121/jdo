package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.parser.IdConfig;
import io.jutil.jdo.core.parser.IdType;
import io.jutil.jdo.internal.core.util.AssertUtil;
import lombok.Setter;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@Setter
public class DefaultIdConfig extends DefaultFieldConfig implements IdConfig {
	private IdType idType;
	private GeneratorType generatorType;

	public DefaultIdConfig() {
	}

	@Override
	public void check() {
		super.check();
		AssertUtil.notNull(idType, "主键类型");
		AssertUtil.notNull(generatorType, "主键生成类型");
	}

	@Override
	public IdType getIdType() {
		return idType;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return generatorType;
	}

}
