package io.jutil.jdo.internal.spring.config;

import io.jutil.jdo.core.engine.JdoBuilder;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.internal.spring.plugin.SpringConnectionHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Jin Zheng
 * @since 2022-05-18
 */
@Configuration
@EnableConfigurationProperties(JdoProperties.class)
@ConditionalOnProperty(prefix = "jdo", name = "enabled", havingValue = "true")
public class JdoAutoConfiguration {
	public JdoAutoConfiguration() {
	}

	@Bean
	public JdoTemplate jdoTemplate(DataSource dataSource, JdoProperties prop) {
		var connectionHolder = new SpringConnectionHolder(dataSource);
		var jdo = JdoBuilder.create()
				.setDataSource(dataSource)
				.setConnectionHolder(connectionHolder)
				.addScanPackages(prop.getScanPackages())
				.setEscape(prop.isEscape())
				.build();
		return jdo.getJdoTemplate();
	}

}
