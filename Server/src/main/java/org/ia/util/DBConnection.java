package org.ia.util;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

import org.bson.Document;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    private MongoClient mongo;
    private Logger mongoLogger;
    private MongoDatabase statsDB;
    MongoCollection<Document> collection;


    public void connectToDB() {
        mongo = new MongoClient( "localhost", 27017);
        mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        statsDB = mongo.getDatabase("statsDB");
    }

    public void connectToCustomHostDB(String host, int port) {
        mongo = new MongoClient(host, port);
        mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        statsDB = mongo.getDatabase("statsDB");
    }

    public void setStatisticsDB() {
        statsDB = mongo.getDatabase("statsDB");
    }

    public void statsAddConnection(ClientRequest clientRequest) {
        Document document = new Document();
        for (RequestData request: clientRequest.requestDataList) {
            document.append(request.getType(), request.getValue());
        }
        collection = statsDB.getCollection("stats");
        collection.insertOne(document);
        System.out.println(document);
    }

    public void statsCountConnections() {
        System.out.println(statsDB.getCollection("stats").countDocuments());
    }

    public void statsPrintConnections() {
        collection = statsDB.getCollection("stats");
        FindIterable<Document> iterDoc = collection.find();
        int i = 1;

        Iterator it = iterDoc.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
            i++;
        }
    }
}
