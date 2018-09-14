package org.chl.config;

import com.mongodb.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.gridfs.GridFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MongoGridFsTemplate extends AbstractMongoConfiguration {
    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    @Bean
    public GridFSBucket gridFSBucket() throws Exception {
        return GridFSBuckets.create(mongoClient().getDatabase("test"));
    }

    @Bean
    public GridFS gridFS() throws Exception {
        return new GridFS(mongoDbFactory().getLegacyDb());
    }

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(host, port);
    }

    @Override
    protected String getDatabaseName() {
        return mongoClient().getDatabase("test").getName();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("30MB");
        factory.setMaxRequestSize("30MB");
        return factory.createMultipartConfig();
    }
}
