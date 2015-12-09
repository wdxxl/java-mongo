package com.wdxxl.mongo.query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;

public class TestQueryDoc {
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
        collection.insertOne(new Document("address",
                                new Document()
                                    .append("street", "2 Avenue")
                                    .append("zipcode", "20075")
                                    .append("building", "2480")
                                    .append("coord", asList(-73.9557413, 40.7720266))
                                )
                                .append("borough", "Banhattan")
                                .append("cuisine", "Italian")
                                .append("grades", asList(
                                     new Document()
                                         .append("date",format.parse("2014-10-01T00:00:00Z"))
                                         .append("grade", "A")
                                         .append("score", 31),
                                     new Document()
                                         .append("date",format.parse("2014-01-16T00:00:00Z"))
                                         .append("grade", "B")
                                         .append("score", 37)))
                                .append("name", "Vella")
                                .append("restaurant_id", "41704620"));
    }

    @After
    public void tearDown() {
        collection.drop();
    }

    // Query for All Documents in a Collection
    @Test
    public void testQueryAllDocsInCollection() {
        FindIterable<Document> iterable = collection.find();
        printDoc(iterable);
    }

    // Specify Equality Conditions (Top Level Field)
    @Test
    public void testTopLevelField() {
        FindIterable<Document> iterableDoc = collection.find(new Document("borough", "Manhattan"));
        printDoc(iterableDoc);

        FindIterable<Document> iterableEq = collection.find(eq("borough", "Manhattan"));
        printDoc(iterableEq);
    }

    // Specify Equality Conditions (Embedded Document)
    @Test
    public void testEmbeddedDocument() {
        FindIterable<Document> iterableDoc =
                collection.find(new Document("address.zipcode", "10075"));
        printDoc(iterableDoc);

        FindIterable<Document> iterableEq = collection.find(eq("address.zipcode", "10075"));
        printDoc(iterableEq);
    }

    // Query by a Field in an Array
    @Test
    public void testFieldInArray() {
        FindIterable<Document> iterableDoc = collection.find(new Document("grades.grade", "B"));
        printDoc(iterableDoc);

        FindIterable<Document> iterableEq = collection.find(eq("grades.grade", "B"));
        printDoc(iterableEq);
    }

    // Specify Conditions with Operators (lt)
    @Test
    public void testLt() {
        FindIterable<Document> iterableDoc =
                collection.find(new Document("grades.score", new Document("$lt", 15)));
        printDoc(iterableDoc);

        FindIterable<Document> iterableLt = collection.find(lt("grades.score", 15));
        printDoc(iterableLt);
    }

    // Specify Conditions with Operators (gt)
    @Test
    public void testGt() {
        FindIterable<Document> iterableDoc =
                collection.find(new Document("grades.score", new Document("$gt", 15)));
        printDoc(iterableDoc);

        FindIterable<Document> iterableGt = collection.find(gt("grades.score", 15));
        printDoc(iterableGt);
    }

    // Combine Conditions(and)
    @Test
    public void testCombineConditionAnd() {
        FindIterable<Document> iterableDoc =
                collection.find(new Document("cuisine", "Italian").append("address.zipcode", "10075"));
        printDoc(iterableDoc);

        FindIterable<Document> iterableAnd =
                collection.find(and(eq("cuisine", "Italian"), eq("address.zipcode", "10075")));
        printDoc(iterableAnd);
    }

    // Combine Conditions(or)
    @Test
    public void testCombineConditionOr() {
        FindIterable<Document> iterableDoc = collection
                .find(new Document("$or", asList(new Document("cuisine","Italian"),
                        new Document("address.zipcode", "10075"))));
        printDoc(iterableDoc);

        FindIterable<Document> iterableOr =
                collection.find(or(eq("cuisine", "Italian"), eq("address.zipcode", "10075")));
        printDoc(iterableOr);
    }

    // Sort Query Results
    @Test
    public void testSortQueryResults() {
        FindIterable<Document> iterable =
                collection.find().sort(new Document("borough", 1).append("address.zipcode", 1));
        printDoc(iterable);

        FindIterable<Document> iterableAsc =
                collection.find().sort(ascending("address.zipcode", "borough"));
        printDoc(iterableAsc);
    }

    private void printDoc(FindIterable<Document> iterableDoc) {
        iterableDoc.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                System.out.println(document);
            }
        });
    }

}
