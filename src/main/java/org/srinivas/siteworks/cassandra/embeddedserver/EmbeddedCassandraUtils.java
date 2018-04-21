/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.cassandra.embeddedserver;

import java.util.concurrent.TimeUnit;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;

public class EmbeddedCassandraUtils {

	private static final String LOCATION_DATASTORE_EMBEDDED_CASSANDRA = "target/embeddedCassandra";
	private static final String FILE_EMBEDDED_CASSANDRA_YAML = "embedded-cassandra.yaml";

	public static void startCassandraEmbedded() throws Exception {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra(FILE_EMBEDDED_CASSANDRA_YAML, LOCATION_DATASTORE_EMBEDDED_CASSANDRA, TimeUnit.SECONDS.toMillis(60));
	}

	public static void stopCassandraEmbedded() {
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}
}
