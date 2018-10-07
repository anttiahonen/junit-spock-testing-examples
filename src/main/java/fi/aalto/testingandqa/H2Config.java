package fi.aalto.testingandqa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "fi.aalto.testingandqa.review")
@EnableTransactionManagement
public class H2Config {

    @Autowired
    Environment environment;

    Properties jpaProperties()
    {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", environment.getProperty( "spring.jpa.show-sql" ) );
        properties.setProperty("hibernate.format_sql", environment.getProperty( "spring.jpa.format-sql" ) );
        properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty( "spring.jpa.hibernate.ddl-auto" ) );
        properties.setProperty("hibernate.dialect", environment.getProperty( "spring.jpa.dialect" ) );
        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager()
    {
        return new JpaTransactionManager( entityManagerFactory().getObject() );
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource( dataSource() );
        factoryBean.setJpaVendorAdapter( jpaVendorAdapter );
        factoryBean.setPackagesToScan( "fi.aalto.testingandqa.review" );
        factoryBean.setJpaProperties( jpaProperties() );
        factoryBean.setPersistenceUnitName( "persistence" );

        return factoryBean;
    }

    @Bean
    @Primary
    @ConfigurationProperties( "spring.datasource" )
    public DataSourceProperties dataSourceProperties()
    {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties( "spring.datasource" )
    public DataSource dataSource()
    {
        return dataSourceProperties().initializeDataSourceBuilder().build();
    }


}