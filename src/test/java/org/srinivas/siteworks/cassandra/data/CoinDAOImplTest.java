/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.cassandra.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.srinivas.siteworks.cassandra.Coin;
import org.srinivas.siteworks.cassandra.embeddedserver.EmbeddedCassandraUtils;
import org.srinivas.siteworks.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class CoinDAOImplTest {

	@Autowired
	CoinDAOImpl coinDAOImpl;

	@Autowired
	CoinRepository coinRepository;

	private Coin coinOne;
	private Coin coinTwo;
	private Coin coinThree;

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
		coinThree.setValue("abcd");

	}

	@After
	public void tearDown() {
		coinDAOImpl.clearCoins();
		coinDAOImpl = null;
	}

	@Test
	public void testSavecoin() {
		Coin result = coinDAOImpl.saveCoin(coinOne);
		assertEquals("descriptionOne", result.getDescription());
	}

	@Test
	public void testRetrievecoin() {
		coinDAOImpl.saveCoin(coinOne);
		Coin result = coinDAOImpl.retrieveCoin(coinOne.getName());
		assertEquals("descriptionOne", result.getDescription());
	}

	@Test
	public void testDeletecoin() {
		coinDAOImpl.saveCoin(coinOne);
		Coin toBeDeletedcoin = coinDAOImpl.retrieveCoin(coinOne.getName());
		coinDAOImpl.deleteCoin(toBeDeletedcoin);
		Coin result = coinDAOImpl.retrieveCoin(coinOne.getName());
		assertNull(result);
	}

	@Test
	public void testUpdatecoin() {
		Coin coin = coinDAOImpl.saveCoin(coinOne);
		coin.setValue("44444");
		coinDAOImpl.updateCoin(coin);
		Coin result = coinDAOImpl.retrieveCoin(coinOne.getName());
		assertEquals("44444", result.getValue());
	}

	@Test
	public void testRepositoryRetrieveByName() {
		coinDAOImpl.saveCoin(coinOne);
		Coin result = coinRepository.findByName(coinOne.getName());
		assertEquals("descriptionOne", result.getDescription());
	}

	@Test
	public void testRepositoryFindAll() {
		coinDAOImpl.saveCoin(coinOne);
		coinDAOImpl.saveCoin(coinTwo);
		Stream<Coin> stream = coinDAOImpl.findAllCoins();
		List<Coin> coinList = stream.collect(Collectors.toList());
		assertTrue(coinList.stream().filter(e -> e.getValue().equals("67890")).findFirst().isPresent());
		assertTrue(coinList.stream().filter(e -> e.getValue().equals("12345")).findFirst().isPresent());
	}

	@Test
	public void testCqlTemplateFindByCoinValue() {
		coinDAOImpl.saveCoin(coinThree);
		List<Coin> coins = coinDAOImpl.findByCoinValue(coinThree.getValue());
		assertTrue(coins.size() == 1);
		assertTrue(coins.stream().filter(e -> e.getName().equals(coinThree.getName())).findFirst().isPresent());
	}
}
