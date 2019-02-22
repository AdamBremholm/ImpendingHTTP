package org.ia.util;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.ia.api.Storage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

//* Mongo database. Connects to remote database.
// All HTTP-requests are saved, as well as all persons added through the form.
//
// */

public class MongoDB implements Storage {

    MongoClientURI uri = new MongoClientURI("mongodb://ImpendingUser:impendingpassword@impendingcluster-shard-00-00-n8qde.mongodb.net:27017,impendingcluster-shard-00-01-n8qde.mongodb.net:27017,impendingcluster-shard-00-02-n8qde.mongodb.net:27017/test?ssl=true&replicaSet=ImpendingCluster-shard-0&authSource=admin&retryWrites=true");

    private MongoClient mongo;
    private Logger mongoLogger;
    private MongoDatabase database;
    MongoCollection<Document> collection;
    MongoCollection<Document> personCollection;

    public MongoDB() {
        mongo = new MongoClient(uri);
        mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        database = mongo.getDatabase("ImpendingHTTP");
        collection = database.getCollection("statistics");
        personCollection = database.getCollection("persons");
    }

    public int getRequestCount() {
        return (int) database.getCollection("statistics").countDocuments();
    }

    @Override
    public void close() {
        System.out.println("Hello, I'm a close method. I'm never called for the mongoDB. " +
                "I should probably be used to close all mongo cursors and the connection to the remote server," +
                "but Patrik prioritized other things over making that happen. #sorrynotsorry");
    }

    @Override
    public void addPerson(Person person) {
        Document doc = new Document();
        BasicDBObject query = new BasicDBObject();

        query.append("name", person.getName());
        query.append("address",person.getAddress());
        query.append("dateOfBirth",person.getDateOfBirth());

        doc.append("name",person.getName());
        doc.append("address",person.getAddress());
        doc.append("dateOfBirth",person.getDateOfBirth());

        personCollection.replaceOne( query, doc, new UpdateOptions().upsert(true)); //Unsafe method. But.. still. It works.
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
    public JSONArray findAllPersons() {

        JSONParser parser = new JSONParser();
        JSONArray array = null;

        String t = StreamSupport.stream(personCollection.find().spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));
        try {
            array = (JSONArray)parser.parse(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return array;
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
        ((MongoCursor) it).close();
        return sb.toString();
    }


    public JSONArray getRequestsAsJsonArray() {

        String t = StreamSupport.stream(collection.find().spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));

        JSONParser parser = new JSONParser();
        JSONArray array = null;
        try {
            array = (JSONArray)parser.parse(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return array;
    }

    public JSONObject getRequestsAsJsonObject() {

        String t = StreamSupport.stream(collection.find().spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(",", "{", "}"));

        System.out.println("String t: " + t);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = (JSONObject)parser.parse(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
