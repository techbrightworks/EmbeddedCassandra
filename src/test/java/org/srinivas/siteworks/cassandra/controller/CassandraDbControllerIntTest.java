/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.cassandra.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.srinivas.siteworks.cassandra.Coin;
import org.srinivas.siteworks.cassandra.data.CoinDAOImpl;
import org.srinivas.siteworks.cassandra.embeddedserver.EmbeddedCassandraUtils;
import org.srinivas.siteworks.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class CassandraDbControllerIntTest {

	private MockMvc mockMvc;

	@Autowired
	CoinDAOImpl coinDAOImpl;

	private static final Logger logger = LoggerFactory.getLogger(CassandraDbControllerIntTest.class);

	@Autowired
	private CassandraDbController cassandraDbController;

	private static Boolean isServerAlreadyStarted = false;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(this.cassandraDbController).build();
	}

	@After
	public void tearDown() throws Exception {
		mockMvc = null;

	}

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
	public void testAddCoin() {

		try {
			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/coin/addCoin?name=One&value=xyz&description=descriptionOne")).andExpect(status().isOk()).andReturn();
			ObjectMapper jsonMapper = new ObjectMapper();
			Coin coin = jsonMapper.readValue(result.getResponse().getContentAsString(), Coin.class);
			assertEquals("xyz", coin.getValue());
		} catch (Exception e) {
			logger.info(e.getMessage());
			fail("Failed Due to: " + e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		MockMvc findAllMockMvc = MockMvcBuilders.standaloneSetup(this.cassandraDbController).build();
		try {
			mockMvc.perform(MockMvcRequestBuilders.post("/coin/addCoin?name=One&value=xyz&description=descriptionOne")).andExpect(status().isOk()).andReturn();
			MvcResult result = findAllMockMvc.perform(MockMvcRequestBuilders.get("/coin/findAll")).andExpect(status().isOk()).andReturn();
			ObjectMapper jsonMapper = new ObjectMapper();
			List<Coin> listCoin = jsonMapper.readValue(result.getResponse().getContentAsString(), jsonMapper.getTypeFactory().constructCollectionType(List.class, Coin.class));
			assertTrue(listCoin.size() == 1);
		} catch (Exception e) {
			logger.info(e.getMessage());
			fail("Failed Due to: " + e.getMessage());
		}

	}

	@Test
	public void testUpdateCoin() {

		MockMvc updateMockMvc = MockMvcBuilders.standaloneSetup(this.cassandraDbController).build();
		try {
			mockMvc.perform(MockMvcRequestBuilders.post("/coin/addCoin?name=One&value=xyz&description=descriptionOne")).andExpect(status().isOk()).andReturn();

			MvcResult result = updateMockMvc.perform(MockMvcRequestBuilders.put("/coin/updateCoin?name=One&value=444&description=descriptionOne")).andExpect(status().isOk()).andReturn();
			ObjectMapper jsonMapper = new ObjectMapper();
			Coin coin = jsonMapper.readValue(result.getResponse().getContentAsString(), Coin.class);
			assertEquals("444", coin.getValue());
		} catch (Exception e) {
			logger.info(e.getMessage());
			fail("Failed Due to: " + e.getMessage());
		}

	}

	@Test
	public void testDeleteCoin() {
		MockMvc deleteMockMvc = MockMvcBuilders.standaloneSetup(this.cassandraDbController).build();
		try {
			mockMvc.perform(MockMvcRequestBuilders.post("/coin/addCoin?name=One&value=xyz&description=descriptionOne")).andExpect(status().isOk()).andReturn();
			deleteMockMvc.perform(MockMvcRequestBuilders.delete("/coin/deleteCoin?name=One&value=xyz&description=descriptionOne")).andExpect(status().isOk()).andReturn();
		} catch (Exception e) {
			logger.info(e.getMessage());
			fail("Failed Due to: " + e.getMessage());
		}

	}

	@Configuration
	static class ChangeControllerTestConfiguration {

		@Bean
		public CassandraDbController cassandraDbController() {
			return new CassandraDbController();
		}
	}

}
