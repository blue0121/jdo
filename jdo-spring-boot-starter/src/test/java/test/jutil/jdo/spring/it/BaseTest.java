package test.jutil.jdo.spring.it;

import io.jutil.jdo.core.engine.JdoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Jin Zheng
 * @since 2022-05-19
 */
@SpringBootTest(classes = BaseTest.Config.class)
public class BaseTest {
	protected JdoTemplate jdoTemplate;

	public BaseTest() {
	}

    @ComponentScan("test.jutil.jdo.spring.model")
    @SpringBootApplication
    public static class Config {
    }

	protected void beforeEach() {
		jdoTemplate.execute("insert into usr_group (id,name,count) values (1,'blue',1)");
		jdoTemplate.execute("insert into usr_group (id,name,count) values (2,'green',1)");
	}

	protected void afterEach() {
		jdoTemplate.execute("truncate table usr_group");
		jdoTemplate.execute("truncate table usr_user");
	}

	@Autowired
	public void setJdoTemplate(JdoTemplate jdoTemplate) {
		this.jdoTemplate = jdoTemplate;
	}
}
