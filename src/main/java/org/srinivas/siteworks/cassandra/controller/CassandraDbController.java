/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.cassandra.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.srinivas.siteworks.cassandra.Coin;
import org.srinivas.siteworks.cassandra.data.CoinDAOImpl;

@RestController
@RequestMapping(value = "/coin", produces = MediaType.APPLICATION_JSON_VALUE)
public class CassandraDbController {

	private static final Logger logger = LoggerFactory.getLogger(CassandraDbController.class);

	@Autowired
	CoinDAOImpl coinDAOImpl;

	@RequestMapping(value = "/addCoin", method = RequestMethod.POST)
	public Coin addCoin(@RequestParam("name") String name, @RequestParam("value") String value, @RequestParam("description") String description) {
		logger.info("CassandraDbController:addCoin Method");
		try {
			Coin coin = new Coin();
			coin.setName(name);
			coin.setValue(value);
			coin.setDescription(description);
			Coin result = coinDAOImpl.saveCoin(coin);
			coinDAOImpl.closeSession();
			return result;
		} catch (Exception e) {
			logger.error("Error:" + e);
			return new Coin();
		}
	}

	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	public @ResponseBody List<Coin> findAllCoins() {
		logger.info("CassandraDbController:findAll Method");
		try {
			List<Coin> result = coinDAOImpl.findAllCoins().collect(Collectors.toList());
			coinDAOImpl.closeSession();
			return result;
		} catch (Exception e) {
			logger.error("Error:" + e);
			return new ArrayList<Coin>();
		}
	}

	@RequestMapping(value = "/updateCoin", method = RequestMethod.PUT)
	public @ResponseBody Coin updateCoin(@RequestParam("name") String name, @RequestParam("value") String value, @RequestParam("description") String description) {
		logger.info("CassandraDbController:updateCoin Method");
		try {
			Coin coin = new Coin();
			coin.setName(name);
			coin.setValue(value);
			coin.setDescription(description);
			Coin result = coinDAOImpl.updateCoin(coin);
			coinDAOImpl.closeSession();
			return result;
		} catch (Exception e) {
			logger.error("Error:" + e);
			return new Coin();
		}
	}

	@RequestMapping(value = "/deleteCoin", method = RequestMethod.DELETE)
	public @ResponseBody void deleteCoin(@RequestParam("name") String name, @RequestParam("value") String value, @RequestParam("description") String description) {
		logger.info("CassandraDbController:deleteCoin Method");
		try {
			Coin coin = new Coin();
			coin.setName(name);
			coin.setValue(value);
			coin.setDescription(description);
			coinDAOImpl.deleteCoin(coin);
			coinDAOImpl.closeSession();
		} catch (Exception e) {
			logger.error("Error:" + e);
		}
	}

}
