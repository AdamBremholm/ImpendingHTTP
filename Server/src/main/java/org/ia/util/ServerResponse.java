package org.ia.util;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServerResponse {

    String serverName = "Impending Server";
    Socket connect;
    byte[] jsonBytes;
    ReadFileData readFileData;
    PrintWriter out = null;
    BufferedOutputStream dataOut = null;
    String firstLine;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    String contentType;
    String contentLength;

    public ServerResponse(Socket connect, ReadFileData readFileData) {
        this.connect = connect;
        this.readFileData = readFileData;
    }


    public void sendGet(ClientRequest clientRequest, File file, ReadFileData readFileData) throws IOException {

        int fileLength = (int) file.length();
        byte[] fileData = readFileData.readFileData(file, fileLength);

        // send HTTP Headers
        out.println("HTTP/1.1 200 OK");
        out.println(serverName);
        out.println("Date: " + new Date());
        out.println("Content-type: " + getContentType());
        out.println("Content-length: " + fileLength);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
    }

    public void initWriters() throws IOException {
        out = new PrintWriter(connect.getOutputStream());
        dataOut = new BufferedOutputStream(connect.getOutputStream());
    }

    public void sendNotImplemented(ClientRequest clientRequest) throws IOException {

        File file = new File(ResourceConfig.WEB_ROOT, ResourceConfig.METHOD_NOT_SUPPORTED);
        int fileLength = (int) file.length();
        clientRequest.setContentType("text/html");

        byte[] fileData = readFileData.readFileData(file, fileLength);

        out.println("HTTP/1.1 501 Not Implemented");
        out.println(serverName);
        out.println("Date: " + new Date());
        out.println("Content-type: " + getContentType());
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
    }


    public void sendPostJson() throws IOException {

        out.println("HTTP/1.1 200 OK");
        out.println(serverName);
        out.println("Date: " + new Date());
        out.println("Content-type: " + "text/html");
        out.println("Content-length: " + jsonBytes.length);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(jsonBytes);
        dataOut.flush();
    }

    public void sendPost(ClientRequest clientRequest, ReadFileData readFileData, File file) throws IOException {
        byte[] fileData = readFileData.readFileData(file, (int)file.length());

        out.println("HTTP/1.1 200 OK");
        out.println(serverName);
        out.println("Date: " + new Date());
        out.println("Content-type: " + getContentType());
        out.println("Content-length: " + fileData.length);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData);
        dataOut.flush();
    }

    //From clientRequest's stringBuilder, from POST body.
    public void setJson(ClientRequest clientRequest) {
        String jsonString = JsonParser.urlToFormattedString(clientRequest.getPayloadString());
        jsonBytes = jsonString.getBytes();
    }

    //From clientRequest's fileRequest, from GET requests
    public void setJson(String fileRequest) {
        String jsonString = JsonParser.urlToFormattedString(fileRequest);
        jsonBytes = jsonString.getBytes();
    }

    public void setJson(JSONObject obj) {
        jsonBytes = obj.toString().getBytes();
    }

    public void close() throws IOException {
        out.close();
        dataOut.close();
    }

    public OutputStream getDataOut() {
        return dataOut;
    }

    public PrintWriter getOut() {
        return out;
    }
}