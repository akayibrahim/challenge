package org.chl.config;

import java.io.IOException;
import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.*;
import com.mongodb.MongoClient;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Configuration
public class MongoConfig {
    private static final String MONGO_DB_URL = "localhost";
    private static final String MONGO_DB_NAME = "chl";

    @Bean
    public MongoTemplate mongoTemplate() throws IOException {
        EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
        mongo.setBindIp(MONGO_DB_URL);
        mongo.setPort(9082);
        MongoClient mongoClient = mongo.getObject();
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGO_DB_NAME);
        return mongoTemplate;
    }
}
