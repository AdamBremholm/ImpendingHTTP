package org.ia.util;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Runnable {

    static final boolean verbose = true;

    // Client Connection via Socket Class
    public Socket connect;
    public ClientConnection(Socket c) {
        connect = c;
    }
    ReadFileData readFileData = new ReadFileData();

    @Override
    public void run() {
        ClientRequest clientRequest = new ClientRequest(connect);
        ServerResponse serverResponse = new ServerResponse(connect, readFileData);
//
//        PrintWriter out = null;
//        BufferedOutputStream dataOut = null;

        try {
            //In, out and dataOut
            clientRequest.initReaders();
            serverResponse.initWriters();

            if (!clientRequest.isGetOrHeadOrPost()) {
                if (verbose) {
                    System.out.println("501 Not Implemented : " + clientRequest.getMethod() + " method.");
                }
                serverResponse.sendNotImplemented(clientRequest);

            } else if (clientRequest.isPost() && clientRequest.bodyExists()) {
                if (clientRequest.getFile().endsWith("/")) {
                    clientRequest.setFile(clientRequest + ResourceConfig.DEFAULT_FILE);
                }

                String clientBody = clientRequest.getPayloadString();
                    if (!clientRequest.isGetOrHead()){
                        //Puts Json in byte[] and sends to client
                        serverResponse.setJson(JsonParser.formatSlicedUrl(clientBody));
                        serverResponse.sendPostJson();
                    } else {
                        serverResponse.sendNotImplemented(clientRequest);
                    }
            } else {
                // GET or HEAD method
                if (clientRequest.getFile().endsWith("/")) {
                    clientRequest.setFile(clientRequest.getFile() + ResourceConfig.DEFAULT_FILE);
                }

                File file = new File(ResourceConfig.WEB_ROOT, clientRequest.getFile());
                // int fileLength = (int) file.length();
                clientRequest.setContentType(readFileData.getContentType(clientRequest.getFile()));

                if (clientRequest.isGet()
                        && clientRequest.getFile().startsWith("/json")
                        && clientRequest.getFile().length() > 5) {
                    serverResponse.setJson(clientRequest.getFile());
                    serverResponse.sendPostJson();

                } else if (clientRequest.isGet()) { // GET method so we return content
                    serverResponse.sendGet(clientRequest, file, readFileData);
                }

                if (verbose) {
                    System.out.println("File " + clientRequest.getFile() + " of type " + clientRequest.getContentType() + " returned");
                }
            }

        } catch (FileNotFoundException fnfe) {
            try {
                readFileData.fileNotFound(serverResponse.getOut(), serverResponse.getDataOut(), clientRequest.getFile()); //TODO: Error här
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            try {
                serverResponse.close();
                clientRequest.close();
                connect.close(); // we close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }
            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }
    }
}