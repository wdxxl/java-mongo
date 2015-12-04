package com.wdxxl.mongo.auth;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.Test;

/**
 * MongoDB 2.4.9 & MongoDB-Java-Driver 3.1.1
 * http://stackoverflow.com/questions/21859579/authentication-during-connection-to-mongodb-server-
 * instance-using-java
 */
public class AuthenticationApp {

    @Test(expected = MongoWriteException.class)
    @SuppressWarnings("resource")
    public void TestMongoWriteException() {
        MongoClient mongo = new MongoClient("localhost", 27017);
        /**** Get Database ****/
        MongoDatabase database = mongo.getDatabase("testdb");
        /**** Get Collection/table from 'testdb' ****/
        MongoCollection<Document> collection = database.getCollection("user");

        Document document = new Document("name", "king.wang");
        collection.insertOne(document);
    }

    @SuppressWarnings("resource")
    @Test
    public void TestAuthenticationAdminSuccess() {
        MongoClient mongo =
                new MongoClient(new MongoClientURI("mongodb://admin:password@localhost/admin"));
        /**** Get Database ****/
        MongoDatabase mongoDatabase = mongo.getDatabase("testdb");
        /**** Get Collection/table from 'testdb' ****/
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");

        Document document = new Document("name", "king.wang");
        collection.insertOne(document);
    }

    @SuppressWarnings("resource")
    @Test
    public void TestAuthenticationTestDBSuccess() {
        MongoClient mongo =
                new MongoClient(new MongoClientURI("mongodb://testdb:password@localhost/testdb"));
        /**** Get Database ****/
        MongoDatabase mongoDatabase = mongo.getDatabase("testdb");
        /**** Get Collection/table from 'testdb' ****/
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");

        Document document = new Document("name", "king.wang2");
        collection.insertOne(document);
    }

}
