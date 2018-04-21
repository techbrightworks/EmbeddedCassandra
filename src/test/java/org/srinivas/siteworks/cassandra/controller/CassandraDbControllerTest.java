/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.cassandra.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.srinivas.siteworks.cassandra.Coin;
import org.srinivas.siteworks.cassandra.data.CoinDAOImpl;
import org.srinivas.siteworks.cassandra.data.CoinRepository;
import org.srinivas.siteworks.cassandra.embeddedserver.EmbeddedCassandraUtils;
import org.srinivas.siteworks.config.AppConfig;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class CassandraDbControllerTest {

	@Autowired
	CassandraDbController cassandraDbController;

	@Autowired
	CoinDAOImpl coinDAOImpl;

	@Autowired
	Cluster embedededcluster;

	@Autowired
	Session embeddedsession;

	@Autowired
	CassandraConverter converter;

	@Autowired
	CoinRepository coinRepository;

	CassandraAdminOperations cassandraAdminTemplate;
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_VALUE = "value";
	private static final String COLUMN_NAME = "name";
	private static final String TABLE_NAME_COINS = "Coins";

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		cassandraAdminTemplate = new CassandraAdminTemplate(embeddedsession, converter);
	}

	@After
	public void tearDown() throws Exception {
		cassandraDbController = null;
	}

	private static Boolean isServerAlreadyStarted = false;

	@BeforeClass
	public static void startEmbedded() throws Exception {
		if (EmbeddedCassandraServerHelper.getSession() == null) {
			EmbeddedCassandraUtils.startCassandraEmbedded();
		} else {
			isServerAlreadyStarted = true;
		}
	}

	@AfterClass
	public static void stopEmbedded() {
		if (!isServerAlreadyStarted) {
			EmbeddedCassandraUtils.stopCassandraEmbedded();
		}
	}

	@Test
	public void testAddCoin() throws Exception {
		Coin coin = cassandraDbController.addCoin("one", "2222", "descriptionone");
		assertEquals("one", coin.getName());
	}

	@Test
	public void testFindAll() throws Exception {
		Coin coin = new Coin();
		coin.setName("one");
		coin.setValue("2222");
		coin.setDescription("descriptionone");
		addTestCoin(coin);
		coin = new Coin();
		coin.setName("Two");
		coin.setValue("3333");
		coin.setDescription("descriptiontwo");
		addTestCoin(coin);
		List<Coin> coins = cassandraDbController.findAllCoins();
		assertTrue(coins.size() == 2);
	}

	@Test
	public void testUpdateCoin() throws Exception {
		Coin coin = new Coin();
		coin.setName("one");
		coin.setValue("2222");
		coin.setDescription("descriptionone");
		addTestCoin(coin);
		Coin updatedCoin = cassandraDbController.updateCoin("one", "6666", "descriptionone");
		assertEquals("6666", updatedCoin.getValue());
	}

	@Test
	public void testDeleteCoin() throws Exception {
		Coin coin = new Coin();
		coin.setName("one");
		coin.setValue("2222");
		coin.setDescription("descriptionone");
		addTestCoin(coin);
		coin = new Coin();
		coin.setName("Two");
		coin.setValue("3333");
		coin.setDescription("descriptiontwo");
		addTestCoin(coin);
		cassandraDbController.deleteCoin("Two", "3333", "descriptiontwo");
		assertTrue(cassandraDbController.findAllCoins().size() == 1);
	}

	public void addTestCoin(Coin coin) {
		if (embeddedsession.isClosed()) {
			embeddedsession = coinDAOImpl.getEmbeddedsession();
			cassandraAdminTemplate = new CassandraAdminTemplate(embeddedsession, converter);
		}
		Insert insert = QueryBuilder.insertInto(TABLE_NAME_COINS).value(COLUMN_NAME, coin.getName()).value(COLUMN_VALUE, coin.getValue()).value(COLUMN_DESCRIPTION, coin.getDescription()).ifNotExists();
		cassandraAdminTemplate.getCqlOperations().execute(insert);
	}

}
