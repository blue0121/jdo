package test.jutil.jdo.id;

import io.jutil.jdo.internal.core.id.EpochOptions;
import io.jutil.jdo.internal.core.id.LongEpochIdGenerator;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public class LongEpochIdGeneratorTest extends IdGeneratorTest<Long> {
	public LongEpochIdGeneratorTest() {
		var options = new EpochOptions();
		options.setIpBits(8);
		options.setSequenceBits(12);
        this.generator = new LongEpochIdGenerator(options);
	}

    @Override
    protected String toString(Long id) {
        return Long.toHexString(id);
    }
}
