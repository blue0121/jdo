package io.jutil.jdo.internal.core.parser.model;

import io.jutil.jdo.core.parser.VersionConfig;
import lombok.Setter;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
@Setter
public class DefaultVersionConfig extends DefaultFieldConfig implements VersionConfig {
	private boolean force;
	private int defaultValue;

	public DefaultVersionConfig() {
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
