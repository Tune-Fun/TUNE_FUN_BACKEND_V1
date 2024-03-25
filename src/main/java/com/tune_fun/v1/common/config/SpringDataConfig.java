package com.tune_fun.v1.common.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.mongodb.MongoMetricsCommandListener;
import io.micrometer.core.instrument.binder.mongodb.MongoMetricsConnectionPoolListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;

@Configuration
@Profile("dev_standalone | dev")
@RequiredArgsConstructor
public class SpringDataConfig {

    @Bean
    public MongoClientFactoryBean mongoClientFactoryBean(MongoProperties properties, MeterRegistry meterRegistry) {
        MongoClientFactoryBean mongoClientFactoryBean = new MongoClientFactoryBean();

        mongoClientFactoryBean.setConnectionString(new ConnectionString(properties.getUri()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .addCommandListener(new MongoMetricsCommandListener(meterRegistry))
                .applyToConnectionPoolSettings(builder ->
                        builder.addConnectionPoolListener(new MongoMetricsConnectionPoolListener(meterRegistry)))
                .build();
        mongoClientFactoryBean.setMongoClientSettings(settings);

        return mongoClientFactoryBean;
    }

}
