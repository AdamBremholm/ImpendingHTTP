package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;

import java.io.IOException;
import java.util.Date;

@Adress("/v1/DynamicWebPage")
public class DynamicWebPage implements org.ia.api.ImpendingInterface {

    //* TODO: Adam, vad tusan gör den? Läser POST payload och skickar ut den dynamiska sidan? Skriv javadoc :p
    //
    // */

    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {

        String postbody = "";
        if (clientRequest.getPayloadString()!=null){
            postbody = clientRequest.getPayloadString();
        }

        String htmlString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Dynamic WebPage Plugin with todays date:" + new Date() + "</h1>\n" +
                "\n" +
                "<p>" + "Please try to send data in postrequestbody and it will display here: (Raw)" + postbody +"</p>\n" +
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
