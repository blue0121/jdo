package io.jutil.jdo.internal.spring.config;

import io.jutil.jdo.core.engine.JdoBuilder;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.internal.spring.plugin.SpringConnectionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Jin Zheng
 * @since 2022-05-18
 */
@Configuration
@EnableConfigurationProperties(JdoProperties.class)
@ConditionalOnProperty(prefix = "jdo", name = "enabled", havingValue = "true")
public class JdoAutoConfiguration {
	private static Logger logger = LoggerFactory.getLogger(JdoAutoConfiguration.class);

	public JdoAutoConfiguration() {
	}

	@Bean
	public JdoTemplate jdoTemplate(DataSource dataSource, JdoProperties prop) {
		var connectionHolder = new SpringConnectionHolder(dataSource);
		var jdo = JdoBuilder.create()
				.setDataSource(dataSource)
				.setConnectionHolder(connectionHolder)
				.addScanPackages(prop.getScanPackages())
				.setInitSql(this.readInitSql(prop.getInitSqlPaths()))
				.build();
		return jdo.getJdoTemplate();
	}

	private String readInitSql(String[] paths) {
		if (paths == null || paths.length == 0) {
			return null;
		}

		StringBuilder sb = new StringBuilder(4096);
		for (var path : paths) {
			try (var is = new FileInputStream(ResourceUtils.getFile(path))){
				var bytes = is.readAllBytes();
				sb.append(new String(bytes, StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
		var sql = sb.toString();
		logger.info("初始化SQL: {}", sql);
		return sql;
	}

}
