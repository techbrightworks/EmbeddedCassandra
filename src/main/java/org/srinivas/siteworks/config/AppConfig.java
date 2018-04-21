/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Scope;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;



@ComponentScan(basePackages = { "org.srinivas.siteworks" }, excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class) })
@Configuration
public class AppConfig {

	@Configuration
	@EnableCassandraRepositories(basePackages = { "org.srinivas.siteworks.cassandra.data" })
	static class CassandraConfig extends AbstractCassandraConfiguration {
		private static final String LOCALHOST = "localhost";
		public static final String KEYSPACE = "coinsdata";

		@Bean
		public Cluster embedededcluster() {
			Cluster cluster = Cluster.builder().addContactPoints(LOCALHOST).withPort(9042).build();
			return cluster;
		}

		@Bean
		public CassandraMappingContext mappingContext() {
			CassandraMappingContext mappingContext = new CassandraMappingContext();
			mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cluster().getObject(), KEYSPACE));
			return mappingContext;
		}

		@Bean
		public CassandraConverter converter() {
			return new MappingCassandraConverter(mappingContext());
		}

		@Bean()
		@Scope("prototype")
		public Session embeddedsession() {
			Session session = embedededcluster().connect(KEYSPACE);
			return session;
		}

		@Override
		public SchemaAction getSchemaAction() {
			return SchemaAction.CREATE_IF_NOT_EXISTS;
		}

		@Override
		protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
			CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(KEYSPACE);
			return Arrays.asList(specification);
		}

		@Override
		protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
			return Arrays.asList(DropKeyspaceSpecification.dropKeyspace(KEYSPACE));
		}

		@Override
		protected String getKeyspaceName() {
			return KEYSPACE;
		}

		@Override
		public String[] getEntityBasePackages() {
			return new String[] { "org.srinivas.siteworks" };
		}

	}

}
