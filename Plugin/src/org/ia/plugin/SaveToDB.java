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


@Adress("/v1/SaveToDB")
public class SaveToDB implements ImpendingInterface {

    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {

        Person person = new Person();
      // final List<Person> personList = new ArrayList<>(); Se om man kan göra en lösning med arrayList

        String htmlString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Add a person to our storage here: </h1>\n" +
                "<form action=\"/v1/SaveToDB\" method=\"post\" onsubmit=\"submitFunction()\">\n" +
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
                    "<h1>Add a person to our storage here: </h1>\n" +
                    "<form action=\"/v1/SaveToDB\" method=\"post\" onsubmit=\"submitFunction()\">\n" +
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
