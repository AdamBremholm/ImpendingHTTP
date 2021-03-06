package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;
import org.ia.util.ClientRequest;
import org.ia.util.JsonParser;
import org.ia.util.Person;
import org.ia.util.ServerResponse;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.ia.util.StorageController.storage;


//*  Sends Back a person in the post request body of the form. If body exists, it displays the person below.
// */

@Adress("/v1/SavePerson")
public class SavePerson implements ImpendingInterface {

    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {

        Person person = new Person();

        String htmlString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Save Person plugin</h1>" +
                "<p> Add a person below and it will be sent back in the body of a post request to this page and printed below<p> \n" +
                "<form action=\"/v1/SavePerson\" method=\"post\" onsubmit=\"submitFunction()\">\n" +
                "    Full name:<br>\n" +
                "    <input type=\"text\" id=\"fullName\" name=\"fullName\" value=\"fullName\">\n" +
                "    <br><br>\n" +
                "    Adress:<br>\n" +
                "    <input type=\"text\" name=\"address\" id=\"address\" value=\"address\">\n" +
                "    <br><br>\n" +
                "    Birth date:<br>\n" +
                "    <input type=\"text\" name=\"dateOfBirth\" id=\"dateOfBirth\" value=\"dateOfBirth\">\n" +
                "    <br><br>\n" +
                "    <input type=\"submit\" value=\"submit\">\n" +
                "</form>" +
                "\n" +
                "<p> </p>\n"+
                "\n" +
                "</body>\n" +
                "</html>\n";

        if (clientRequest.isPost()) {

            JSONObject jsonObject = JsonParser.formatSlicedUrl(clientRequest.getPayloadString());
            person.setName(jsonObject.get("fullName").toString());
            person.setAdress(jsonObject.get("address").toString());
            person.setDateOfBirth(jsonObject.get("dateOfBirth").toString());

            String htmlString2 = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>Save Person plugin</h1>" +
                    "<p> Add a person below and it will be sent back in the body of a post request to this page and printed below<p> \n" +
                    "<form action=\"/v1/SavePerson\" method=\"post\" onsubmit=\"submitFunction()\">\n" +
                    "    Full name:<br>\n" +
                    "    <input type=\"text\" id=\"fullName\" name=\"fullName\" value=\"fullName\">\n" +
                    "    <br><br>\n" +
                    "    Adress:<br>\n" +
                    "    <input type=\"text\" name=\"address\" id=\"address\" value=\"address\">\n" +
                    "    <br><br>\n" +
                    "    Birth date:<br>\n" +
                    "    <input type=\"text\" name=\"dateOfBirth\" id=\"dateOfBirth\" value=\"dateOfBirth\">\n" +
                    "    <br><br>\n" +
                    "    <input type=\"submit\" value=\"submit\">\n" +
                    "</form>" +
                    "\n" +
                    "<p>Here is the person: " + person + "</p>\n"+
                    "\n" +
                    "</body>\n" +
                    "</html>\n";
            try {
                serverResponse.sendPostHTML(htmlString2);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                serverResponse.sendPostHTML(htmlString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return serverResponse;
    }
}
