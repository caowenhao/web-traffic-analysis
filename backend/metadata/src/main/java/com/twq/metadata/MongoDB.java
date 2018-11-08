package com.twq.metadata;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

    private static String mongodbAddr = System.getProperty("web.metadata.mongodbAddr", "localhost");

    private static MongoClient mongoClient = new MongoClient(mongodbAddr);

    public static MongoDatabase getMongoDatabase(String dbName) {
        return mongoClient.getDatabase(dbName);
    }
}
