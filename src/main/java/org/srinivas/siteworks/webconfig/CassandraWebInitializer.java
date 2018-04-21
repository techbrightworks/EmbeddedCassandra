/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.webconfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class CassandraWebInitializer implements WebApplicationInitializer {

	private static final Logger logger = LoggerFactory.getLogger(CassandraWebInitializer.class);

	@Override
	public void onStartup(ServletContext container) {
		logger.info("Started to pickup the annotated classes at CassandraWebInitializer");
		startServlet(container);
	}

	private void startServlet(final ServletContext container) {
		WebApplicationContext dispatcherContext = registerContext(CassandraMvcContextConfiguration.class);
		DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
		container.addListener(new ContextLoaderListener(dispatcherContext));
		ServletRegistration.Dynamic dispatcher;
		dispatcher = container.addServlet("dispatcher", dispatcherServlet);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}

	private WebApplicationContext registerContext(final Class<?>... annotatedClasses) {
		logger.info("Using AnnotationConfigWebApplicationContext createContext");
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(annotatedClasses);
		return context;
	}

}
