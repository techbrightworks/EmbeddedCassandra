/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.cassandra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CoinTest {

	private Coin coin;

	@Before
	public void setUp() throws Exception {
		coin = new Coin();
	}

	@After
	public void tearDown() throws Exception {
		coin = null;
	}

	@Test
	public void testGetName() {
		assertNull(coin.getName());
		coin.setName("One Pound");
		assertEquals("One Pound", coin.getName());
	}

	@Test
	public void testSetName() {
		coin.setName("One Pound");
		assertEquals("One Pound", coin.getName());
	}

	@Test
	public void testSetValue() {
		coin.setValue("1234");
		assertEquals("1234", coin.getValue());
	}

	@Test
	public void testGetValue() {
		assertNull(coin.getValue());
		coin.setValue("1234");
		assertEquals("1234", coin.getValue());
	}

	@Test
	public void testSetDescription() {
		coin.setDescription("Descriptionone");
		assertEquals("Descriptionone", coin.getDescription());
	}

	@Test
	public void testGetDescription() {
		assertNull(coin.getDescription());
		coin.setDescription("Descriptionone");
		assertEquals("Descriptionone", coin.getDescription());
	}

}
