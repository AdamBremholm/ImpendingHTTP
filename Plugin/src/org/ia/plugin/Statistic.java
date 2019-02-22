package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.api.Storage;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.util.Date;

import static org.ia.util.StorageController.storage;


@Adress("/v1/Statistic")
public class Statistic implements org.ia.api.ImpendingInterface {


    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {




        String htmlString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Dynamic WebPage Plugin with todays date:" + new Date() + "</h1>\n" +
                "\n" +
                "<p>" + "Here is statistics for the get request sent to the server" + storage.getRequestsAsJsonArray().toString() + "</p>\n" +
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
