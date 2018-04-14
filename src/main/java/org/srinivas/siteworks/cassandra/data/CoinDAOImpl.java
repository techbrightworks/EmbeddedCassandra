package org.srinivas.siteworks.cassandra.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.stereotype.Repository;
import org.srinivas.siteworks.cassandra.Coin;

import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Truncate;

@Repository
public class CoinDAOImpl {

	@Autowired 
	CassandraAdminOperations cassandraTemplate;
	

	/**
	 * Save Coin.	
	 * @param coin the Coin
	 * @return the Coin
	 */
	public Coin saveCoin(Coin coin) {
		Insert insert = QueryBuilder.insertInto("Coins").value("name", coin.getName()) 
				.value("value", coin.getValue()) 
				.value("description", coin.getDescription()) 
				.ifNotExists(); 
		
		cassandraTemplate.getCqlOperations().execute(insert);
		Coin result  = cassandraTemplate.selectOneById(coin.getName(), Coin.class);
		return result;
	}

	/**
	 * Retrieve Coin.
	 * @param name the name
	 * @return the Coin
	 */
	public Coin retrieveCoin(String name) {
		Coin result  = cassandraTemplate.selectOneById(name, Coin.class);
		return result;
	}

	/**
	 * Delete Coin.	
	 * @param Coin the Coin
	 */
	public void deleteCoin(Coin coin) {
		cassandraTemplate.delete(coin);
		
	}

	/**
	 * Update Coin.
	 * @param name the name
	 * @return the Coin
	 */
	public Coin updateCoin(Coin coin) {
		cassandraTemplate.update(coin);
		Coin result  = cassandraTemplate.selectOneById(coin.getName(), Coin.class);
		return result;
	}

	/**
	 * Clear Coins.	
	 */
	public void clearCoins() {
		Truncate truncate  = QueryBuilder.truncate("coinsdata", "Coins");
		 cassandraTemplate.getCqlOperations().execute(truncate);
		
	}
	
	
	public CassandraAdminOperations getCassandraTemplate() {
		return cassandraTemplate;
	}

	public void setCassandraTemplate(CassandraAdminOperations cassandraTemplate) {
		this.cassandraTemplate = cassandraTemplate;
	}

}
