package io.jutil.jdo.internal.core.id;

import io.jutil.jdo.internal.core.util.NumberUtil;

/**
 * @author Jin Zheng
 * @since 2022-05-26
 */
public class StringIdGenerator implements IdGenerator<String> {
	private final ByteArrayIdGenerator idGenerator;

	public StringIdGenerator() {
		idGenerator = new ByteArrayIdGenerator();
	}

	@Override
	public String generate() {
		var id = idGenerator.generate();
		return NumberUtil.toHexString(id);
	}
}
