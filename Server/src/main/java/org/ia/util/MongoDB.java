package org.ia.util;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters.*;

import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.ia.api.Storage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;

public class MongoDB implements Storage {

    MongoClientURI uri = new MongoClientURI("mongodb://ImpendingUser:impendingpassword@impendingcluster-shard-00-00-n8qde.mongodb.net:27017,impendingcluster-shard-00-01-n8qde.mongodb.net:27017,impendingcluster-shard-00-02-n8qde.mongodb.net:27017/test?ssl=true&replicaSet=ImpendingCluster-shard-0&authSource=admin&retryWrites=true");

    private MongoClient mongo;
    private Logger mongoLogger;
    private MongoDatabase database;
    MongoCollection<Document> collection;
    MongoCollection<Document> personCollection;
    String user = "ImpendingUser";
    String password = "impendingpassword";

    public MongoDB() {
        MongoCredential credential = MongoCredential.createCredential(user, "database", password.toCharArray());
        mongo = new MongoClient(uri);
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
        BasicDBObject query = new BasicDBObject();

        query.append("name", person.getName());
        query.append("address",person.getAddress());
        query.append("dateOfBirth",person.getDateOfBirth());


//        doc.append("_id",(int)personCollection.countDocuments()+1);
        doc.append("name",person.getName());
        doc.append("address",person.getAddress());
        doc.append("dateOfBirth",person.getDateOfBirth());

//        and(
//                eq ("name",person.getName()),
//                eq ("address",person.getAddress()),
//                eq ("dateOfBirth",person.getDateOfBirth())),

//        personCollection.insertOne(doc);
        personCollection.replaceOne( query, doc, new UpdateOptions().upsert(true));
//        personCollection.updateOne(doc, doc);
    }

    @Override
    public String findFirstPerson(String searchParam) {
        Document doc;
        JSONObject obj = JsonParser.formatSlicedUrl(searchParam);

        doc = personCollection.find(and( eq ("name", obj.get("name").toString()),
                eq ("address",obj.get("address").toString()),
                eq ("dateOfBirth",obj.get("dateOfBirth").toString()))).first();
        return doc.toJson();

    }

    @Override
    public ArrayList<String> findAllPersons() {
        ArrayList<String> list = new ArrayList<>();
//
//        FindIterable<Document> results = personCollection.find(); //.projection(excludeId());
//        MongoCursor<Document> cursor = results.iterator();
//
//        while (cursor.hasNext()) {
//            list.add(cursor.next().toJson());
//        }
//        cursor.close(); // Håll koll - ska den stängas? Kan ge fel.
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
            i++;
        }
        ((MongoCursor) it).close(); // Håll koll - ska den stängas? Kan ge fel.
        return sb.toString();
    }
}
