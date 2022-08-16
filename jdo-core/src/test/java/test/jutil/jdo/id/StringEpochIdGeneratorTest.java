package test.jutil.jdo.id;

import io.jutil.jdo.internal.core.id.EpochOptions;
import io.jutil.jdo.internal.core.id.StringEpochIdGenerator;

/**
 * @author Jin Zheng
 * @since 2022-08-16
 */
public class StringEpochIdGeneratorTest extends IdGeneratorTest<String> {
	public StringEpochIdGeneratorTest() {
        var options = new EpochOptions();
        this.generator = new StringEpochIdGenerator(options);
	}

    @Override
    protected String toString(String id) {
        return id;
    }

}
