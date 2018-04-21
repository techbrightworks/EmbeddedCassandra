/**
 * @author SrinivasJasti
 * 
 */
package org.srinivas.siteworks.webconfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.cassandra.embeddedserver.EmbeddedCassandraUtils;

@WebListener
public final class CassandraListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(CassandraListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("Started CassandraListener ServletContext destroy");
		EmbeddedCassandraUtils.stopCassandraEmbedded();
		logger.info("Cleaned Keyspace CassandraListener ServletContext destroy");
		if (EmbeddedCassandraServerHelper.getSession() != null) {
			EmbeddedCassandraServerHelper.getSession().close();
			EmbeddedCassandraServerHelper.getCluster().close();
		}
		logger.info("CassandraListener ServletContext destroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("Started CassandraListener ServletContext initialization");
		try {
			EmbeddedCassandraUtils.startCassandraEmbedded();
		} catch (Exception e) {
			logger.error("Failed to listen", e);
		}
		logger.info("CassandraListener ServletContext initialized");
	}

}