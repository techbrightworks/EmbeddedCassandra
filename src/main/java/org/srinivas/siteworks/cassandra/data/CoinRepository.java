package org.srinivas.siteworks.cassandra.data;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.srinivas.siteworks.cassandra.Coin;

public interface CoinRepository extends CassandraRepository<Coin,Long> {
	
	@Query("SELECT*FROM coins WHERE name=?0")
    Coin findByCoinName(String name);
 
    @Query("SELECT*FROM coins WHERE value=?0")
    List<Coin> findByCoinValue(String value);

}
