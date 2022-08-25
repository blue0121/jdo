package io.jutil.jdo.internal.core.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 不重复 ID 产生器
 *
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class IdGeneratorFactory {
	private static Logger logger = LoggerFactory.getLogger(IdGeneratorFactory.class);

	private final EpochOptions options;
	private final IdGenerator<String> generator;

	public IdGeneratorFactory() {
		this(new EpochOptions());
	}

	public IdGeneratorFactory(EpochOptions options) {
		this.options = options;
		this.generator = new String20IdGenerator(options);
		logger.info("初始化 IdGenerator: {}", generator.getClass().getSimpleName());
	}

	public String uuid() {
		return generator.generate();
	}

}
