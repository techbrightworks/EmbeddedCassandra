package org.srinivas.siteworks.cassandra.data;


import java.util.concurrent.TimeUnit;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.srinivas.siteworks.cassandra.Coin;
import org.srinivas.siteworks.config.AppConfig;

import junit.framework.TestCase;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
@SpringBootTest
public class CoinDAOImplTest extends TestCase {

	@Autowired
	CoinDAOImpl coinDAOImpl;
	
	@Autowired 
	private CassandraAdminOperations cassandraAdminTemplate;
	
	@Autowired
    CoinRepository coinRepository;
	
	private Coin coinOne;
	private Coin coinTwo;
	private Coin coinThree;

	@BeforeClass
	public static void startCassandraEmbedded() throws Exception  { 
		EmbeddedCassandraServerHelper.startEmbeddedCassandra("embedded-cassandra.yaml", "target/embeddedCassandra",
		TimeUnit.SECONDS.toMillis(60));
	}
	
	@AfterClass
	public static void stopCassandraEmbedded() {
	    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}

	@Before
	public void setUp() {
		coinOne = new Coin();
		coinOne.setName("coinOne");
		coinOne.setValue("12345");
		coinOne.setDescription("descriptionOne");
		coinTwo = new Coin();
		coinTwo.setName("coinTwo");
		coinTwo.setDescription("descriptionTwo");
		coinTwo.setValue("67890");
		coinThree = new Coin();
		coinThree.setName("coinThree");
		coinThree.setDescription("descriptionThree");
	    coinThree.setValue("987654");
	    coinDAOImpl.cassandraTemplate =cassandraAdminTemplate;
	}

	

	 
	public void tearDown() {
	    coinDAOImpl.clearCoins();
		coinDAOImpl = null;
	}
 
	/**
	 * Test save coin.
	 */
	@Test
	public void testSavecoin() {
		Coin result = coinDAOImpl.saveCoin(coinOne);
		assertEquals("descriptionOne", result.getDescription());
	}

	/**
	 * Test retrieve coin.
	 */
	@Test
	public void testRetrievecoin() {
		coinDAOImpl.saveCoin(coinOne);
		Coin result = coinDAOImpl.retrieveCoin(coinOne.getName());
		assertEquals("descriptionOne", result.getDescription());
	}

	/**
	 * Test delete coin.
	 */
	@Test
	public void testDeletecoin() {
		coinDAOImpl.saveCoin(coinOne);
		Coin toBeDeletedcoin = coinDAOImpl.retrieveCoin(coinOne.getName());
		coinDAOImpl.deleteCoin(toBeDeletedcoin);
		Coin result = coinDAOImpl.retrieveCoin(coinOne.getName());
		assertNull(result);
	}

	/**
	 * Test update coin.
	 */
	@Test
	public void testUpdatecoin() {
	    Coin coin =	coinDAOImpl.saveCoin(coinOne);
	    coin.setValue("44444");
		coinDAOImpl.updateCoin(coin);
		Coin result = coinDAOImpl.retrieveCoin(coinOne.getName());
		assertEquals("44444", result.getValue());
	}
	
	@Test
	public void testRetrieveByRepository() {
		coinDAOImpl.saveCoin(coinOne);
		Coin result = coinRepository.findByCoinName(coinOne.getName());
		assertEquals("descriptionOne", result.getDescription());
	}
	
}
