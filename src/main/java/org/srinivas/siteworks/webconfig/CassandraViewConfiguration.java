/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.webconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

@Configuration
public class CassandraViewConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(CassandraViewConfiguration.class);

	@Bean
	public ViewResolver viewResolver() {
		logger.info("ChangeViewConfiguration viewResolver()");
		return new ContentNegotiatingViewResolver();
	}

}
