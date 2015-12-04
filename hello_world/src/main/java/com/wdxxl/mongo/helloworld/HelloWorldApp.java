package com.wdxxl.mongo.helloworld;

import java.util.Date;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;

/**
 * MongoDB 2.4.9 & MongoDB-Java-Driver 3.1.1
 * http://mongodb.github.io/mongo-java-driver/3.1/driver/getting-started/quick-tour/
 */
public class HelloWorldApp {

    private static MongoClient mongo;

    public static void main(String[] args) {
        mongo = new MongoClient("localhost", 27017);

        /**** Get Database ****/
        MongoDatabase database = mongo.getDatabase("testdb");

        /**** Get Collection/table from 'testdb' ****/
        MongoCollection<Document> collection = database.getCollection("user");

        /**** Insert Document ****/
        System.out.println("/**** Insert Document ****/");
        Document document = new Document();
        document.put("name", "king.wang");
        document.put("age", 29);
        document.put("createdDate", new Date());
        collection.insertOne(document);

        /**** FindIterable ****/
        System.out.println("/**** FindIterable ****/");
        Document findFilter = new Document();
        findFilter.put("name", "king.wang");
        FindIterable<Document> docIterator = collection.find(findFilter);
        for (Document doc : docIterator) {
            System.out.println(doc.toJson());
        }

        /***** Update Document ****/
        System.out.println("/**** Update Document ****/");
        Document updateDoc = new Document("$set", new Document("name", "king.update.wang"));
        UpdateResult result = collection.updateMany(Filters.eq("name", "king.wang"), updateDoc);
        System.out.println("Update Matched Count:" + result.getMatchedCount());

        /**** MongoCursor ****/
        System.out.println("/**** MongoCursor ****/");
        Document findUpdateFilter = new Document();
        findUpdateFilter.put("name", "king.update.wang");
        MongoCursor<Document> cursor = collection.find(findUpdateFilter).iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        /**** Delete Document ****/
        Document deleteFilter = new Document();
        deleteFilter.put("name", "king.update.wang");
        collection.deleteMany(deleteFilter);

        /**** Done ****/
        System.out.println("/**** Done ****/");
    }

}
