package org.srinivas.siteworks.cassandra.data;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.srinivas.siteworks.cassandra.Coin;

public interface CoinRepository extends CassandraRepository<Coin,String> {
	
	@Query("SELECT*FROM coins WHERE name=?0")
    Iterable<Coin> findByElementName(String name);
 
    @Query("SELECT*FROM coins WHERE value=?0")
    Iterable<Coin> findByElementValue(String value);

}
