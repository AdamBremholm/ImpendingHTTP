package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.ia.util.StorageController.storage;

//* Prints various server statistics.
// Reads all request headers from database and parses into something more interesting.
// */

@Adress("/v1/Statistic")
public class Statistic implements org.ia.api.ImpendingInterface {


    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {

        JSONArray jsonArray = storage.getRequestsAsJsonArray();


        long postReqs = jsonArray.stream().filter(o -> ((JSONObject) o).get("Method").equals("POST")).count();
        long getReqs = jsonArray.stream().filter(o -> ((JSONObject) o).get("Method").equals("GET")).count();
        long headRequest = jsonArray.stream().filter(o -> ((JSONObject) o).get("Method").equals("HEAD")).count();
        long dynamicWebPageCount = jsonArray.stream().filter(o -> ((String) ((JSONObject) o).get("File")).contains("dynamicwebpage")).count();
        long findInDBCount = jsonArray.stream().filter(o -> ((String) ((JSONObject) o).get("File")).contains("person")).count();
        long jsonWriterCount = jsonArray.stream().filter(o -> ((String) ((JSONObject) o).get("File")).contains("jsonwriter")).count();
        long savePersonCount = jsonArray.stream().filter(o -> ((String) ((JSONObject) o).get("File")).contains("saveperson")).count();
        long statisticCount = jsonArray.stream().filter(o -> ((String) ((JSONObject) o).get("File")).contains("statistic")).count();


        String htmlString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1> Statistic Plugin (live since 2018-02-22)</h1>\n" +
                "\n" +
                "<p>" + "Post Request sent to server: " + postReqs + "</p>\n" +
                "<p>" + "Get request sent to server: " + getReqs + "</p>\n" +
                "<p>" + "Head request sent to server: " + headRequest + "</p>\n" +
                "<p>" + "Number of times different plugins has been called:  </p>\n" +
                "<p>" + "DynamicWebPage: " + dynamicWebPageCount + "</p>\n" +
                "<p>" + "FindinDBPlugin (Person): " + findInDBCount + "</p>\n" +
                "<p>" + "jsonWriterCount : " + jsonWriterCount + "</p>\n" +
                "<p>" + "SavePerson : " + savePersonCount + "</p>\n" +
                "<p>" + "Statistics : " + statisticCount + "</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";

        try {
            serverResponse.sendPostHTML(htmlString);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return serverResponse;
    }
}
