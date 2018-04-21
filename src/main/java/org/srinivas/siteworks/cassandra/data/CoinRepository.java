/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.cassandra.data;

import java.util.stream.Stream;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.Repository;
import org.srinivas.siteworks.cassandra.Coin;

public interface CoinRepository extends Repository<Coin, String> {

	Coin findByName(String name);

	@Query("SELECT name,value,description FROM coins")
	Stream<Coin> findAll();
}
