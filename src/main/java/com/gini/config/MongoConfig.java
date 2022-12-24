package com.gini.config;

import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

// https://stackoverflow.com/questions/47055743/spring-data-mongodb-where-to-create-an-index-programmatically-for-a-mongo-coll
// https://www.baeldung.com/spring-data-mongodb-reactive
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
        ReactiveMongoTemplate mongoTemplate = new ReactiveMongoTemplate(mongoClient, databaseName);
        mongoTemplate.indexOps("customer") // collection name string or .class
                .ensureIndex(
                        new Index().on("username", Sort.Direction.ASC).unique()
                ).subscribe();

        return mongoTemplate;
    }
}
