package ia.org.client;

import java.io.IOException;

public class RunClient {

    public static void main(String[] args) {

        // issue the Get request
        Client client = new Client();
        String getResponse = null;
        try {
            getResponse = client.doGetRequest("http://localhost:8080/?123");
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        System.out.println(getResponse);


        // issue the post request
/*
        String json = client.bowlingJson("Jesse", "Jake");
        String postResponse = client.doPostRequest("http://www.roundsapp.com/post", json);
        System.out.println(postResponse);

        */


    }



}
