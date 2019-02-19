package org.ia.util;


import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientRequest {

    String firstLine;
    String method;
    String file = null;
    String accept;
    String body = null;
    Socket connect;
    StringBuilder payload;
    String contentType;

    BufferedReader in = null;
    PrintWriter out = null;
    BufferedOutputStream dataOut = null;

    public void initReaders() throws IOException {
        in = new BufferedReader(new InputStreamReader(connect.getInputStream()));

        // get first line of the request from the client
        String input = in.readLine();
        // we parse the request with a string tokenizer
        StringTokenizer parse = new StringTokenizer(input);
        method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
        // we get file requested
        file = parse.nextToken().toLowerCase();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public ClientRequest(String method, String file, String accept, String body) {
        this.method = method;
        this.file = file;
        this.accept = accept;
        this.body = body;
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

    public void readPost() throws IOException {
        while ((body = in.readLine()).length() != 0){
            System.out.println(body);
        }

        payload = new StringBuilder();
        while(in.ready()){
            payload.append((char) in.read());
        }
        System.out.println("Payload data is: "+getPayloadString());
    }

    public String getPayloadString() {
        return payload.toString();
    }

    public void close() throws IOException {
        in.close();
    }
}