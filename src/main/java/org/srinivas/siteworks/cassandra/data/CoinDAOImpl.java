/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.cassandra.data;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.cql.CqlOperations;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.core.cql.RowMapper;
import org.springframework.stereotype.Repository;
import org.srinivas.siteworks.cassandra.Coin;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.datastax.driver.core.querybuilder.Truncate;

@Repository
public class CoinDAOImpl {

	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_VALUE = "value";
	private static final String COLUMN_NAME = "name";
	private static final String KEYSPACE_COINSDATA = "coinsdata";
	private static final String TABLE_NAME_COINS = "Coins";
	CassandraAdminOperations cassandraAdminTemplate;
	CqlOperations cqlTemplate;
	public static final String KEYSPACE = "coinsdata";

	@Autowired
	ApplicationContext context;

	@Autowired
	CoinRepository coinRepository;

	@Autowired
	Cluster embedededcluster;

	@Autowired
	Session embeddedsession;

	@Autowired
	CassandraConverter converter;

	public Coin saveCoin(Coin coin) {
		cassandraAdminTemplate = generateCassandraAdminTemplate();
		Insert insert = QueryBuilder.insertInto(TABLE_NAME_COINS).value(COLUMN_NAME, coin.getName()).value(COLUMN_VALUE, coin.getValue()).value(COLUMN_DESCRIPTION, coin.getDescription()).ifNotExists();
		cassandraAdminTemplate.getCqlOperations().execute(insert);
		Coin result = cassandraAdminTemplate.selectOneById(coin.getName(), Coin.class);
		return result;
	}

	public Coin retrieveCoin(String name) {
		Coin result = generateCassandraAdminTemplate().selectOneById(name, Coin.class);
		return result;
	}

	public void deleteCoin(Coin coin) {
		generateCassandraAdminTemplate().delete(coin);
	}

	public Coin updateCoin(Coin coin) {
		cassandraAdminTemplate = generateCassandraAdminTemplate();
		cassandraAdminTemplate.update(coin);
		Coin result = cassandraAdminTemplate.selectOneById(coin.getName(), Coin.class);
		return result;
	}

	public void clearCoins() {
		Truncate truncate = QueryBuilder.truncate(KEYSPACE_COINSDATA, TABLE_NAME_COINS);
		generateCassandraAdminTemplate().getCqlOperations().execute(truncate);
	}

	public List<Coin> findByCoinValue(String value) {
		Select selectQuery = QueryBuilder.select(COLUMN_NAME, COLUMN_VALUE, COLUMN_DESCRIPTION).from(KEYSPACE_COINSDATA, TABLE_NAME_COINS);
		Where selectWhere = selectQuery.where();
		Clause rkClause = QueryBuilder.eq(COLUMN_VALUE, value);
		selectWhere.and(rkClause);
		List<Coin> coins = generateCqlTemplate().query(selectQuery, new RowMapper<Coin>() {
			public Coin mapRow(Row row, int rowNum) {
				Coin coin = new Coin();
				coin.setName(row.getString(COLUMN_NAME));
				coin.setValue(row.getString(COLUMN_VALUE));
				coin.setDescription(row.getString(COLUMN_DESCRIPTION));
				return coin;
			}
		});
		return coins;
	}

	public Stream<Coin> findAllCoins() {
		Stream<Coin> stream = coinRepository.findAll();
		return stream;
	}

	public CassandraAdminOperations generateCassandraAdminTemplate() {
		return new CassandraAdminTemplate(getEmbeddedsession(), converter);
	}

	public CqlOperations generateCqlTemplate() {
		return new CqlTemplate(getEmbeddedsession());
	}

	public Session getEmbeddedsession() {
		if (embeddedsession.isClosed()) {
			embeddedsession = (Session) context.getBean("embeddedsession");
			return embeddedsession;
		} else {
			return embeddedsession;
		}
	}

	public void setEmbeddedsession(Session embeddedsession) {
		this.embeddedsession = embeddedsession;
	}

	public void closeSession() {
		embeddedsession.close();
	}

	public void closeCluster() {
		embedededcluster.close();
	}

}
