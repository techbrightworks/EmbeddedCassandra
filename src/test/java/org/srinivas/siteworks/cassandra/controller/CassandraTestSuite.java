/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.cassandra.controller;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.srinivas.siteworks.cassandra.CoinTest;
import org.srinivas.siteworks.cassandra.data.CoinDAOImplTest;
import org.srinivas.siteworks.cassandra.embeddedserver.EmbeddedCassandraUtils;

@RunWith(value = Suite.class)
@SuiteClasses(value = { CoinDAOImplTest.class, CassandraDbControllerIntTest.class, CassandraDbControllerIntTest.class, CoinTest.class })
public class CassandraTestSuite {
	@BeforeClass
	public static void startEmbedded() throws Exception {
		EmbeddedCassandraUtils.startCassandraEmbedded();
	}

	@AfterClass
	public static void stopEmbedded() {
		EmbeddedCassandraUtils.stopCassandraEmbedded();
	}
}
