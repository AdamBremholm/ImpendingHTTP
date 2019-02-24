package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;

import java.io.IOException;
import java.util.Date;

@Adress("/v1/DynamicWebPage")
public class DynamicWebPage implements org.ia.api.ImpendingInterface {

    //* If user sends in a body in their post request it will display it in the raw format.
    //  Also shows time and date :)
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
                " <form action=\"/v1/DynamicWebPage\" method=\"POST\">\n" +
                "   \n" +
                "    <p>If you dont have insomnia, you can try it with the form below</p>\n" +
                "                Name:\n" +
                "    <br>\n" +
                "    <input type=\"text\" name=\"name\" id=\"pfullName\" value=\"name\">\n" +
                "    <br><br>\n" +
                "                Address:<br>\n" +
                "    <input type=\"text\" name=\"address\" id=\"paddress\" value=\"address\">\n" +
                "    <br><br>\n" +
                "                Birth date:<br>\n" +
                "    <input type=\"text\" name=\"dateOfBirth\" id=\"pdateOfBirth\" value=\"dateOfBirth\">\n" +
                "    <br><br>\n" +
                "    <input type=\"submit\" value=\"submit\">\n" +
                "</form>" +
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
