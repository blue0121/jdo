package io.jutil.jdo.internal.spring.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Jin Zheng
 * @since 2022-05-19
 */
@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "jdo")
public class JdoProperties implements InitializingBean {
	private String[] scanPackages;
	private boolean escape = true;

	@Override
	public void afterPropertiesSet() {
		if (scanPackages == null || scanPackages.length == 0) {
			throw new IllegalArgumentException("扫描包不能为空");
		}
	}
}
