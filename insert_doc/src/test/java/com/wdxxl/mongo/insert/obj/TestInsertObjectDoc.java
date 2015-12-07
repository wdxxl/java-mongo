package com.wdxxl.mongo.insert.obj;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

public class TestInsertObjectDoc {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        MongoClient client = new MongoClient("localhost", 27017);

        MongoDatabase database = client.getDatabase("testdb");

        CodecRegistry codecRegistry =
                CodecRegistries.fromRegistries(CodecRegistries.fromCodecs(new PersonCodec()),
                        MongoClient.getDefaultCodecRegistry());
        /**** Get Collection/table from 'testdb' ****/
        MongoCollection<Person> collection =
                database.getCollection("user", Person.class).withCodecRegistry(codecRegistry);

        Person p1 = new Person("king.wang1", 29);
        Person p2 = new Person("king.wang2", 29);
        List<Person> persons = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);
        collection.insertMany(persons);

        Person person = collection.find().first();
        System.out.println(person.toString());
    }
}
