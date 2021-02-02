package com.societygeneral.congfig;

import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.societygeneral.repo", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
@EnableTransactionManagement
public class JpaConfiguration {

	@Autowired
	private Environment environment;

	@Value("${datasource.demo.maxPoolSize:10}")
	private int maxPoolSize;

	/*
	 * Populate SpringBoot DataSourceProperties object directly from application.yml
	 * based on prefix.Thanks to .yml, Hierachical data is mapped out of the box
	 * with matching-name properties of DataSourceProperties object].
	 */
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "datasource.demo")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	/*
	 * Configure HikariCP pooled DataSource.
	 */
	@Bean
	public DataSource dataSource() {
		DataSourceProperties dataSourceProperties = dataSourceProperties();
		HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create(dataSourceProperties.getClassLoader())
				.driverClassName(dataSourceProperties.getDriverClassName()).url(dataSourceProperties.getUrl())
				.username(dataSourceProperties.getUsername()).password(dataSourceProperties.getPassword())
				.type(HikariDataSource.class).build();
		dataSource.setMaximumPoolSize(maxPoolSize);
		return dataSource;
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(@Qualifier("dataSource") final DataSource dataSource) {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource("./data.sql"));
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
		return dataSourceInitializer;
	}

	/*
	 * Entity Manager Factory setup.
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setPackagesToScan(new String[] { "com.societygeneral.model" });
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		factoryBean.setJpaProperties(jpaProperties());
		return factoryBean;
	}

	/*
	 * Provider specific adapter.
	 */
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		return hibernateJpaVendorAdapter;
	}

	/*
	 * Here you can specify any provider specific properties.
	 */
	private Properties jpaProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("datasource.demo.hibernate.dialect"));
		properties.put("hibernate.hbm2ddl.auto",
				environment.getRequiredProperty("datasource.demo.hibernate.hbm2ddl.method"));
		properties.put("hibernate.show_sql", environment.getRequiredProperty("datasource.demo.hibernate.show_sql"));
		properties.put("hibernate.format_sql", environment.getRequiredProperty("datasource.demo.hibernate.format_sql"));
		if (StringUtils.isNotEmpty(environment.getRequiredProperty("datasource.demo.defaultSchema"))) {
			properties.put("hibernate.default_schema",
					environment.getRequiredProperty("datasource.demo.defaultSchema"));
		}
		return properties;
	}

	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(emf);
		return txManager;
	}

}
