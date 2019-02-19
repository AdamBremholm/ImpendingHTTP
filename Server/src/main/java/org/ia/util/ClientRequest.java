
package org.ia.util;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ClientRequest {

    String method;
    String file = null;
    String body = null;
    Socket connect;
    StringBuilder payload;
    String contentType;
    List<RequestData> requestObjectsList;


    BufferedReader in = null;
    private String protocol;

    /**
     * Metod som används för att läsa GET och HEAD förfrågningar.
     */
    public void initReaders() throws IOException {
        in = new BufferedReader(new InputStreamReader(connect.getInputStream()));

        StringBuilder builder = new StringBuilder();
        String line;
        requestObjectsList = new ArrayList<>();

        do
        {
            line = in.readLine();
            if (line.equals("")) break;
            //Bygger upp en lång sträng som den sparar. Används för att kunna läsa ut metoden och filen, se nedan.
            builder.append(line);
            builder.append(System.lineSeparator());

            //Delar upp en rad i två delar mellan med kolontecken som separator. Det som är till vänster blir
            // type det andra blir value. Sparas sedan in som RequestData Objekt i arraylisten requestObjectsList.
            StringTokenizer tokenizer = new StringTokenizer(line, ":");
            if (line.contains(":")) {
                String type = tokenizer.nextToken();
                String value = tokenizer.nextToken();
                requestObjectsList.add(new RequestData(type, value));

            }

        }
        while (true);

        StringTokenizer builderParse = new StringTokenizer(builder.toString());
        method = builderParse.nextToken().toUpperCase(); // we get the HTTP method of the client
        // we get file requested
        file = builderParse.nextToken().toLowerCase();
        protocol = builderParse.nextToken().toUpperCase();
        //Adds method, file and protocol to our requestObjectList
        requestObjectsList.add(0, new RequestData("method", method));
        requestObjectsList.add(1, new RequestData("file", file));
        requestObjectsList.add(2, new RequestData("protocol", protocol));


        System.out.println("Prints data from requestObject ArrayList:\n");
        for (RequestData r : requestObjectsList) {
            System.out.println(r.toString());
        }



    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ClientRequest(Socket connect) {
        this.connect = connect;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMethod() {
        return method;
    }

    public String getFile() { return file;    }

    public boolean isGet() {
        if (method.equals("GET")) {
            return true;
        } else return false;
    }

    public boolean isHead() {
        if (method.equals("HEAD")) {
            return true;
        } else return false;
    }

    public boolean isGetOrHead() {
        return (isHead() || isGet());
    }

    public boolean isGetOrHeadOrPost() { return isGetOrHead() || isPost(); }

    public boolean isPost() {
        if (method.equals("POST")) {
            return true;
        } else return false;
    }

    public String readPost() throws IOException {
        while ((body = in.readLine()).length() != 0){
            System.out.println(body);
        }

        payload = new StringBuilder();
        while(in.ready()){
            payload.append((char) in.read());
        }
        System.out.println("Payload data is: "+getPayloadString());
        return payload.toString();
    }

    public String getPayloadString() {
        return payload.toString();
    }

    public void close() throws IOException {
        in.close();
    }
}