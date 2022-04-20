package test.jutil.jdo.executor.parameter;

import io.jutil.jdo.internal.core.executor.parameter.ParameterBinderFacade;
import io.jutil.jdo.internal.core.parser.ConfigCache;
import io.jutil.jdo.internal.core.parser.ParserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseBinderTest {
	protected static final int ONE = 1;
	protected static final int ZERO = 0;

	@Mock
    protected PreparedStatement pstmt;
	@Mock
	protected ResultSet rs;
	@Mock
	protected ParserFactory parserFactory;
	@Mock
	protected ConfigCache configCache;

	protected ParameterBinderFacade facade;

	public BaseBinderTest() {
	}

	@BeforeEach
	public void beforeEach() throws SQLException {
		facade = new ParameterBinderFacade(parserFactory);
		Mockito.doReturn(true, false).when(rs).next();
	}

}
