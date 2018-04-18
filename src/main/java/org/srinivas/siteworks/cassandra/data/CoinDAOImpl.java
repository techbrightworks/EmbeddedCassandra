package org.srinivas.siteworks.cassandra.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.cql.CqlOperations;
import org.springframework.data.cassandra.core.cql.RowMapper;
import org.springframework.stereotype.Repository;
import org.srinivas.siteworks.cassandra.Coin;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.datastax.driver.core.querybuilder.Truncate;

@Repository
public class CoinDAOImpl {

	@Autowired 
	CassandraAdminOperations cassandraAdminTemplate;
	
	@Autowired
    CoinRepository coinRepository;
	
	@Autowired
	CqlOperations cqlTemplate;
	
	
	
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
		
		cassandraAdminTemplate.getCqlOperations().execute(insert);
		Coin result  = cassandraAdminTemplate.selectOneById(coin.getName(), Coin.class);
		return result;
	}

	/**
	 * Retrieve Coin.
	 * @param name the name
	 * @return the Coin
	 */
	public Coin retrieveCoin(String name) {
		Coin result  = cassandraAdminTemplate.selectOneById(name, Coin.class);
		return result;
	}

	/**
	 * Delete Coin.	
	 * @param Coin the Coin
	 */
	public void deleteCoin(Coin coin) {
		cassandraAdminTemplate.delete(coin);
		
	}

	/**
	 * Update Coin.
	 * @param name the name
	 * @return the Coin
	 */
	public Coin updateCoin(Coin coin) {
		cassandraAdminTemplate.update(coin);
		Coin result  = cassandraAdminTemplate.selectOneById(coin.getName(), Coin.class);
		return result;
	}
	

	/**
	 * Clear Coins.	
	 */
	public void clearCoins() {
		Truncate truncate  = QueryBuilder.truncate("coinsdata", "Coins");
		 cassandraAdminTemplate.getCqlOperations().execute(truncate);
	}
	
	public List<Coin> findByCoinValue(String value){
		 Select selectQuery = QueryBuilder.select("name","value","description").from("coinsdata", "Coins");
	     Where selectWhere = selectQuery.where();
	     Clause rkClause = QueryBuilder.eq("value", value);
	     selectWhere.and(rkClause);
	     List<Coin> description = cqlTemplate.query(
				 selectQuery,
					new RowMapper<Coin>() {
						public Coin mapRow(Row row ,int rowNum) {
							Coin coin = new Coin();
							coin.setName(row.getString("name"));
							coin.setValue(row.getString("value"));
							coin.setDescription(row.getString("description"));
							return coin;
						}
					});

			return description;								
	}
		
	public CassandraAdminOperations getCassandraTemplate() {
		return cassandraAdminTemplate;
	}

	public void setCassandraTemplate(CassandraAdminOperations cassandraTemplate) {
		this.cassandraAdminTemplate = cassandraTemplate;
	}

}
