package com.wdxxl.mongo.insert.obj;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class PersonCodec implements Codec<Person> {

    @Override
    public void encode(BsonWriter writer, Person value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("name", value.getName());
        writer.writeInt32("age", value.getAge());
        writer.writeEndDocument();
    }

    @Override
    public Class<Person> getEncoderClass() {
        return Person.class;
    }

    @Override
    public Person decode(BsonReader reader, DecoderContext decoderContext) {
        Person person = new Person();
        reader.readStartDocument();
        reader.readObjectId("_id");
        person.setName(reader.readString("name"));
        person.setAge(reader.readInt32("age"));
        reader.readEndDocument();
        return person;
    }

}
