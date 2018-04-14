package org.srinivas.siteworks.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

@ComponentScan(basePackages = { "org.srinivas.siteworks" })
@Configuration
public class AppConfig {

	@Configuration
	@EnableCassandraRepositories()
	static class CassandraConfig extends AbstractCassandraConfiguration {
		public static final String KEYSPACE = "coinsdata";

		@Bean
		public Cluster embedededcluster() {
			Cluster cluster = Cluster.builder().addContactPoints("localhost").build();
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

		@Bean
		public Session embeddedsession() {

			Session session = embedededcluster().connect(KEYSPACE);

			return session;
		}

		@Bean
		public CassandraAdminOperations cassandraAdminTemplate() throws Exception {
			return new CassandraAdminTemplate(session().getObject(), converter());
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

		public static void main(String[] args) {
			SpringApplication.run(AppConfig.class, args);
		}
	}
}
