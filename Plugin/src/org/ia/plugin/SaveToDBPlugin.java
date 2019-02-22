package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;
import org.ia.util.ClientRequest;
import org.ia.util.Person;
import org.ia.util.ServerResponse;

import java.io.IOException;
import java.util.Date;

import static org.ia.util.StorageController.storage;


@Adress("/v1/SaveToDB")
public class SaveToDBPlugin implements ImpendingInterface {

    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {

        Person person = new Person();



        if (clientRequest.isPost() && clientRequest.bodyExists()) {

        } else {

        //Skickar tillbaka till sig själv vid submit

        String htmlString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Add a person to our storage here: </h1>\n" +
                "<form action=\"/SaveToDBPlugin\" method=\"post\" onsubmit=\"submitFunction()\">\n" +
                "    Full name:<br>\n" +
                "    <input type=\"text\" id=\"fullName\" name=\"fullName\" value=\"fullName\">\n" +
                "    <br><br>\n" +
                "    Adress:<br>\n" +
                "    <input type=\"text\" name=\"address\" id=\"address\" value=\"address\">\n" +
                "    <br><br>\n" +
                "    Birth date:<br>\n" +
                "    <input type=\"text\" name=\"dateOfBirth\" id=\"dateOfBirth\" value=\"dateOfBirth\">\n" +
                "    <br><br>\n" +
                "    Gender<br>\n" +
                "    <select name=\"gender\">\n" +
                "        <option id=\"Male\" value=\"Male\"> Male</option>\n" +
                "        <option id=\"Female\" value=\"Female\">Female</option>\n" +
                "    </select>\n" +
                "    <br><br>\n" +
                "    <input type=\"submit\" value=\"submit\">\n" +
                "</form>" +
                "\n" +
                "<p></p>\n"+
                "+\n" +
                "</body>\n" +
                "</html>\n";

        try {
            serverResponse.sendPostHTML(htmlString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        }


        return serverResponse;
    }

}
