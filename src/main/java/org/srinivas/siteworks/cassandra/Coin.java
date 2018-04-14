package org.srinivas.siteworks.cassandra;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType;

@Table(value = "Coins")
public class Coin {

	@PrimaryKey("name")
	@CassandraType(type = DataType.Name.TEXT)
	private String name;

	@Column("value")
	private String value;

	@Column("description")
	private String description;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return String.format("{ @type = %1$s, name = %2$s, value = %3$s, description = %4$s }", getClass().getName(),
				getName(), getValue(), getDescription());
	}
}
