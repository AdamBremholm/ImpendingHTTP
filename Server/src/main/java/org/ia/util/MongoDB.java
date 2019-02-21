package org.ia.util;

import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;

import org.bson.Document;
import org.ia.api.Storage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Projections.*;

public class MongoDB implements Storage {

    private MongoClient mongo;
    private Logger mongoLogger;
    private MongoDatabase database;
    MongoCollection<Document> collection;
    MongoCollection<Document> personCollection;

    public MongoDB() {
        mongo = new MongoClient( "localhost", 27017);
        mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        database = mongo.getDatabase("ImpendingHTTP");
        collection = database.getCollection("stats");
        personCollection = database.getCollection("persons");
    }

    public int getRequestCount() {
        return (int) database.getCollection("stats").countDocuments();
    }

    @Override
    public void close() {

    }

    @Override
    public void addPerson(Person person) {
        Document doc = new Document();

        doc.put("name",person.getName());
        doc.put("address",person.getAddress());
        doc.put("dateOfBirth",person.getDateOfBirth());

        personCollection.insertOne(doc);
    }

    @Override
    public String findFirstPerson(String searchParam) {
//        personCollection = database.getCollection("persons");
//        Document doc = personCollection.find();
        return null;
    }

    @Override
    public ArrayList<String> findAllPersons() {
        ArrayList<String> list = new ArrayList<>();

        FindIterable<Document> results = personCollection.find().projection(excludeId());
        MongoCursor<Document> cursor = results.iterator();

        while (cursor.hasNext()) {
            list.add(cursor.next().toJson());
        }
        cursor.close(); // Håll koll - ska den stängas? Kan ge fel.
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
