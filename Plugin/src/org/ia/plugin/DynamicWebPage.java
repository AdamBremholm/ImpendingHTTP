package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;


import java.io.IOException;


@Adress("/v1/DynamicWebPage")
public class DynamicWebPage implements org.ia.api.ImpendingInterface {


    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {

        String htmlString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Dynamic WebPage Plugin</h1>\n" +
                "\n" +
                "<p>It Works.</p>\n" +
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
