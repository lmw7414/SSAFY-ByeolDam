//package com.ssafy.star.common.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
//import org.springframework.data.mongodb.core.convert.DbRefResolver;
//import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
//import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
//import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
//
//@Configuration
//public class MongoDBConfig {
//
//    @Value("${spring.data.mongodb.uri}")
//    private String uri;
//
//    @Bean
//    public MongoDatabaseFactory mongoDatabaseFactory() {
//        return new SimpleMongoClientDatabaseFactory(uri);
//    }
//    @Bean
//    public MongoTemplate mongoTemplate() {
//        return new MongoTemplate(mongoDatabaseFactory());
//    }
//
////    @Bean
////    public MappingMongoConverter mappingMongoConverter(
////            MongoDatabaseFactory mongoDatabaseFactory,
////            MongoMappingContext mongoMappingContext
////    ) {
////        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
////        return new MappingMongoConverter(dbRefResolver, mongoMappingContext);
////    }
//}
