package org.ia.util;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//* Parses header information and stores it.
// Holds verification methods, e. g. isPost.
// */

public class ClientRequest {

    public String method;
    public String file = null;
    public Socket connect;
    public StringBuilder payload;
    public String contentType;
    public List<RequestData> requestDataList;

    public BufferedReader in = null;
    private String protocol;

    public void initReaders() throws IOException {
        in = new BufferedReader(new InputStreamReader(connect.getInputStream()));

        StringBuilder builder = new StringBuilder();
        String line;
        requestDataList = new ArrayList<>();

        do {
            line = in.readLine();
            if (line.equals("")) break;
            //Bygger upp en lång sträng som den sparar. Används för att kunna läsa ut metoden och filen, se nedan.
            builder.append(line);
            builder.append(System.lineSeparator());

            //Delar upp en rad i två delar mellan med kolontecken som separator. Det som är till vänster blir
            // type det andra blir value. Sparas sedan in som RequestData Objekt i arraylisten requestDataList.
            StringTokenizer tokenizer = new StringTokenizer(line, ": ");
            if (line.contains(":")) {
                String type = tokenizer.nextToken();
                String value = tokenizer.nextToken();
                requestDataList.add(new RequestData(type, value));
            }
        } while (true);

        StringTokenizer builderParse = new StringTokenizer(builder.toString());
        method = builderParse.nextToken().toUpperCase(); // we get the HTTP method of the client
        // we get file requested
        file = builderParse.nextToken().toLowerCase();
        protocol = builderParse.nextToken().toUpperCase();
        //Adds method, file and protocol to our requestObjectList
        requestDataList.add(0, new RequestData("Method", method));
        requestDataList.add(1, new RequestData("File", file));
        requestDataList.add(2, new RequestData("Protocol", protocol));

        printRequestObjectList();

        //kollar om det är post och om det finns en body med och kör nedre delen av readPostmetoden
        payload = new StringBuilder();
        if (isPost() && bodyExists()) {
            while(in.ready()){
                payload.append((char) in.read());
            }
            if (getPayloadString()!=null && getPayloadString().length()>0) {
                requestDataList.add(new RequestData("Body", getPayloadString()));
            }
        }
    }

    public boolean bodyExists() {
        if (findFirstValueByType("Content-Length")!=null && !findFirstValueByType("Content-Length").equals("")) {
            return Integer.parseInt(findFirstValueByType("Content-Length")) > 0;
        } else return false;
    }

    public void printRequestObjectList() {
        for (RequestData r : requestDataList) {
            System.out.println(r.toString());
        }
    }

    public String findFirstValueByType(String type) {
        String value = "";
        try {
            value = requestDataList.stream().filter(requestData -> requestData.getType().equals(type)).findFirst().get().getValue();
        } catch (Exception e) {
            return "";
        } return value;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ClientRequest(Socket connect) {
        this.connect = connect;
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

    public String getPayloadString() {
        return payload.toString();
    }

    public void close() throws IOException {
        in.close();
    }
}