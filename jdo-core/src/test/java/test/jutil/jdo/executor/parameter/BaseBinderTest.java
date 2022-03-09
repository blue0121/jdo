package test.jutil.jdo.executor.parameter;

import io.jutil.jdo.internal.core.executor.parameter.ParameterBinderFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@ExtendWith(MockitoExtension.class)
public class BaseBinderTest {
	@Mock
    protected PreparedStatement pstmt;

	protected ParameterBinderFactory factory = new ParameterBinderFactory();

	public BaseBinderTest() {
	}

}
