package com.wdxxl.mongo.insert;

import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestInsertDoc {
    private MongoClient mongo;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Before
    public void setUp() {
        mongo = new MongoClient("localhost", 27017);
        /**** Get Database ****/
        database = mongo.getDatabase("testdb");
        /**** Get Collection/table from 'testdb' ****/
        collection = database.getCollection("user");
    }

    @After
    public void tearDown() {
        System.out.println("/**** FindIterable ****/");
        Document findFilter = new Document();
        FindIterable<Document> docIterator = collection.find(findFilter);
        for (Document doc : docIterator) {
            System.out.println(doc.toJson());
        }

        System.out.println("/**** MongoCursor ****/");
        BsonDocument findFilter2 = new BsonDocument("foo", new BsonInt32(1));
        MongoCursor<Document> cursor = collection.find(findFilter2).iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        System.out.println("/**** FindIterable ****/");
        BasicDBObject findFilter3 = new BasicDBObject("foo", "bar");
        FindIterable<Document> docIterator3 = collection.find(findFilter3);
        for (Document doc : docIterator3) {
            System.out.println(doc.toJson());
        }
    }

    @Test
    public void TestMongoJsonInsertDoc() {
        String json = "{'foo':'bar'}";
        Document document = Document.parse(json);
        // { "_id" : ObjectId("56652aa332c89e0d836c13ed"), "foo" : "bar" }
        collection.insertOne(document);
    }

    @Test
    public void TestMongoInsertDoc() {
        Document document = new Document().append("a", "Mongo").append("b", Arrays.asList(1, 2))
                .append("foo", 1);
        // { "_id" : { "$oid" : "5665482832c89e1177f2df0b" }, "a" : "Mongo", "b" : [1, 2], "foo" : 1
        // }
        collection.insertOne(document);
    }
}
