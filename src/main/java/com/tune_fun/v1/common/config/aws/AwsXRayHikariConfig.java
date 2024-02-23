package com.tune_fun.v1.common.config.aws;

import com.tune_fun.v1.common.config.annotation.OnlyDevelopmentConfiguration;
import com.tune_fun.v1.external.aws.xray.AwsXRayTracingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@OnlyDevelopmentConfiguration
public class AwsXRayHikariConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "hikariDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource hikariDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource(HikariDataSource hikariDataSource) {
        //wrap the spring ds into xray tracing ds
        return new AwsXRayTracingDataSource(hikariDataSource);
    }

}
