package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;
import org.ia.util.ClientRequest;
import org.ia.util.JsonParser;
import org.ia.util.ServerResponse;
import org.json.simple.JSONObject;

import java.io.IOException;

@Adress("/v1/calculator")
public class Calculator implements ImpendingInterface {



    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {


        JSONObject jsonObject = JsonParser.formatSlicedUrl(clientRequest.getPayloadString());

        //Creates integers and reformats the strings to int
        int x = Integer.parseInt( jsonObject.get("x").toString());
        int y = Integer.parseInt( jsonObject.get("y").toString());
        int a = x * y;

        //Takes the int and puts it into a byte array with a String then returns the result in Json format
        String aString = ""+a;
        byte [] result = aString.getBytes();
        serverResponse.setJsonBytes(result);
        try {
            serverResponse.sendPostJson();
        } catch (IOException i) {
            i.printStackTrace();
        }



        return serverResponse;
    }
}
