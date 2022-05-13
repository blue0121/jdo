package io.jutil.jdo.internal.core.parser2.model;

import io.jutil.jdo.core.parser2.VersionMetadata;
import lombok.Setter;

/**
 * @author Jin Zheng
 * @since 2022-05-12
 */
@Setter
public class DefaultVersionMetadata extends DefaultFieldMetadata implements VersionMetadata {
	private boolean force;
	private int defaultValue;

	public DefaultVersionMetadata() {
	}

    @Override
    public boolean isForce() {
        return force;
    }

    @Override
    public int getDefaultValue() {
        return defaultValue;
    }
}
