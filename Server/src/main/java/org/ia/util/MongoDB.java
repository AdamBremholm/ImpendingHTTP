package org.ia.util;

import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.Document;
import org.bson.json.JsonReader;
import org.ia.api.Storage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDB implements Storage {

    private MongoClient mongo;
    private Logger mongoLogger;
    private MongoDatabase database;
    MongoCollection<Document> collection;
    MongoCollection<Person> personCollection;

    public MongoDB() {
        mongo = new MongoClient( "localhost", 27017);
        mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        database = mongo.getDatabase("ImpendingHTTP");
        collection = database.getCollection("stats");
        personCollection = database.getCollection("persons", Person.class);
    }

    public int getRequestCount() {
        return (int) database.getCollection("stats").countDocuments();
    }

    @Override
    public void close() {

    }

    @Override
    public void addPerson(Person person) {
        personCollection.insertOne(person);

//        collection.insertOne(new Document().parse("{\"blue\":\"groon\",\"name\":\"adam\",\"skor\":\"true\"}"));
    }

    @Override
    public String findFirstPerson(String searchParam) {
//        personCollection = database.getCollection("persons");
//        Document doc = personCollection.find();

        return null;

    }

    @Override
    public JSONArray findAllPersons() {
        JSONArray list = new JSONArray();

        StringBuilder sb = new StringBuilder();
        FindIterable<Person> iterDoc = personCollection.find();
        int i = 1;

        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {

            list.add(new JSONObject( (JSONObject) it.next()));
            i++;
        }
        ((MongoCursor) it).close(); // Håll koll - ska den stängas? Kan ge fel.
        return list;
    }

    @Override
    public int getPersonCount() {
        return (int) database.getCollection("persons").countDocuments();
    }

    @Override
    public void addRequest(ClientRequest clientRequest) {
        Document document = new Document();
        for (RequestData request: clientRequest.requestDataList) {
            document.append(request.getType(), request.getValue());
        }
        collection.insertOne(document);
        System.out.println(document);
    }

    @Override
    public String getRequests() {
        StringBuilder sb = new StringBuilder();
        FindIterable<Document> iterDoc = collection.find();
        int i = 1;

        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            sb.append(it.next() + "\n");
//            System.out.println(it.next());
            i++;
        }
        ((MongoCursor) it).close(); // Håll koll - ska den stängas? Kan ge fel.
        return sb.toString();
    }
}