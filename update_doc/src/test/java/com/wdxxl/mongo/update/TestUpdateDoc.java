package com.wdxxl.mongo.update;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;

public class TestUpdateDoc {
    private MongoClient mongo;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Before
    public void setUp() throws ParseException {
        mongo = new MongoClient("localhost", 27017);
        /**** Get Database ****/
        database = mongo.getDatabase("testdb");
        /**** Get Collection/table from 'testdb' ****/
        collection = database.getCollection("restaurants");

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        collection.insertOne(new Document("address",
                                 new Document()
                                     .append("street", "2 Avenue")
                                     .append("zipcode", "10075")
                                     .append("building", "1480")
                                     .append("coord", asList(-73.9557413, 40.7720266))
                                 )
                                 .append("borough", "Manhattan")
                                 .append("cuisine", "Italian")
                                 .append("grades", asList(
                                      new Document()
                                          .append("date",format.parse("2014-10-01T00:00:00Z"))
                                          .append("grade", "A")
                                          .append("score", 11),
                                      new Document()
                                          .append("date",format.parse("2014-01-16T00:00:00Z"))
                                          .append("grade", "B")
                                          .append("score", 17)))
                                 .append("name", "Vella")
                                 .append("restaurant_id", "41704620"));
    }

    @After
    public void tearDown() {
        System.out.println("/**** FindIterable ****/");
        Document findFilter = new Document();
        FindIterable<Document> docIterator = collection.find(findFilter);
        for (Document doc : docIterator) {
            System.out.println(doc.toJson());
        }

        collection.drop();
    }

    // Update Top-Level Fields
    @Test
    public void testUpdateTopLevelField() {
        String json = "{'name':'Vella'}";
        Document document = Document.parse(json);
        Document updateDoc = new Document("$set", new Document("name", "Vella 1"));
        collection.updateOne(document, updateDoc);
    }

    // Update an Embedded Field
    // (To update a field within an embedded document, use the dot notation. )
    @Test
    public void testUpdateEmbeddedField() {
        String json = "{'name':'Vella'}";
        Document document = Document.parse(json);
        Document updateDoc =
                new Document("$set", new Document("address.street", "East 31st Street"));
        collection.updateOne(document, updateDoc);
    }

    // Update Multiple Documents
    // To update multiple documents, use the updateMany method.
    @Test
    public void testUpdateMultiple() {
        Document document = new Document("address.zipcode", "10075").append("cuisine", "Italian");
        Document updateDoc =
                new Document("$set", new Document("cuisine", "Category To Be Determined"));
        collection.updateMany(document, updateDoc);
    }

    @Test
    // To replace the entire document except for the _id field
    public void testReplaceOne() {
        String json = "{'name':'Vella'}";
        Document document = Document.parse(json);
        collection.replaceOne(document, new Document("name2", "king.wang"));
        // { "_id" : { "$oid" : "5666a2e432c89e327bd413ea" }, "name2" : "king.wang" }
    }

    // Increment value of embedded array
    @Test
    public void testUpdateArrayIncDoc() {
        String json = "{'grades.grade':'A'}";
        Document document = Document.parse(json);
        Document updateDoc = new Document("$inc", new Document("grades.$.score", 11));
        collection.updateMany(document, updateDoc);
    }


}
