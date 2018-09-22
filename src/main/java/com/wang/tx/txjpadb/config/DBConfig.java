package com.wang.tx.txjpadb.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by wxl on 2018/9/22.
 *
 * @author wxl
 */
@Configuration
public class DBConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.ds.user")
    public DataSourceProperties userDataSourceProperties(){
        return new DataSourceProperties();
    }
    @Bean
    @Primary
    public DataSource userDataSource(){
        return userDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }



    @Bean
    @ConfigurationProperties(prefix = "spring.ds.order")
    public DataSourceProperties orderDataSourceProperties(){
        return new DataSourceProperties();
    }
    @Bean
    public DataSource orderDataSource(){
        return orderDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }
    @Bean
    public JdbcTemplate orderJdbcTemplate(@Qualifier("orderDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }



    @Bean
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager userTM = new JpaTransactionManager();
        userTM.setEntityManagerFactory(entityManagerFactory().getObject());
        DataSourceTransactionManager orderTM = new DataSourceTransactionManager(orderDataSource());
        ChainedTransactionManager tm = new ChainedTransactionManager(userTM,orderTM);
        return tm;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(userDataSource());
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.wang.tx.txjpadb");
        return factory;
    }





}
