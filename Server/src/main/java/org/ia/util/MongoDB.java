package org.ia.util;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;

import org.bson.Document;
import org.ia.api.Storage;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDB implements Storage {

    private MongoClient mongo;
    private Logger mongoLogger;
    private MongoDatabase database;
    MongoCollection<Document> collection;

    public MongoDB() {
        mongo = new MongoClient( "localhost", 27017);
        mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        database = mongo.getDatabase("ImpendingHTTP");
    }

    public int getRequestCount() {
        return (int) database.getCollection("stats").countDocuments();
    }

    @Override
    public void close() {

    }

    @Override
    public void addRequest(ClientRequest clientRequest) {
        Document document = new Document();
        for (RequestData request: clientRequest.requestDataList) {
            document.append(request.getType(), request.getValue());
        }
        collection = database.getCollection("stats");
        collection.insertOne(document);
        System.out.println(document);
    }

    @Override
    public String getRequests() {
        StringBuilder sb = new StringBuilder();
        collection = database.getCollection("stats");
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
