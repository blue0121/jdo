package test.jutil.jdo.id;

import io.jutil.jdo.internal.core.id.EpochOptions;
import io.jutil.jdo.internal.core.id.String20IdGenerator;

/**
 * @author Jin Zheng
 * @since 2022-08-16
 */
public class String20IdGeneratorTest extends IdGeneratorTest<String> {
	public String20IdGeneratorTest() {
        var options = new EpochOptions();
        this.generator = new String20IdGenerator(options);
	}

    @Override
    protected String toString(String id) {
        return id;
    }

}
