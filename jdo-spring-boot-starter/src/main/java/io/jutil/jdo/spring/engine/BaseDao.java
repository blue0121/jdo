package io.jutil.jdo.spring.engine;

import io.jutil.jdo.core.engine.JdoTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jin Zheng
 * @since 2022-05-19
 */
public class BaseDao<T> extends io.jutil.jdo.core.engine.BaseDao<T> {

	@Autowired
	@Override
	public void setJdoTemplate(JdoTemplate jdoTemplate) {
		super.setJdoTemplate(jdoTemplate);
	}
}
