package test.jutil.jdo.id;

import io.jutil.jdo.internal.core.id.String24IdGenerator;

/**
 * @author Jin Zheng
 * @since 2022-08-17
 */
public class String24IdGeneratorTest extends IdGeneratorTest<String> {
	public String24IdGeneratorTest() {
        generator = new String24IdGenerator();
	}

    @Override
    protected String toString(String id) {
        return id;
    }

}
