package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.parser.ColumnConfig;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@Setter
@NoArgsConstructor
public class DefaultColumnConfig extends DefaultFieldConfig implements ColumnConfig {
	private boolean mustInsert;
	private boolean mustUpdate;


	@Override
	public boolean isMustInsert() {
		return mustInsert;
	}

	@Override
	public boolean isMustUpdate() {
		return mustUpdate;
	}
}
