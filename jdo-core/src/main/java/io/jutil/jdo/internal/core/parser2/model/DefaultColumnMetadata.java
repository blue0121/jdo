package io.jutil.jdo.internal.core.parser2.model;

import io.jutil.jdo.core.parser2.ColumnMetadata;
import lombok.Setter;

/**
 * @author Jin Zheng
 * @since 2022-05-12
 */
@Setter
public class DefaultColumnMetadata extends DefaultFieldMetadata implements ColumnMetadata {
	private boolean mustInsert;
	private boolean mustUpdate;

	public DefaultColumnMetadata() {
	}

    @Override
    public boolean isMustInsert() {
        return mustInsert;
    }

    @Override
    public boolean isMustUpdate() {
        return mustUpdate;
    }
}
