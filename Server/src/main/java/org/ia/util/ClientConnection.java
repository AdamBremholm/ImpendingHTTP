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

            }

            else if (clientRequest.getFile().endsWith("/") && !clientRequest.isPost()) {
                clientRequest.setFile("/" + ResourceConfig.DEFAULT_FILE);
                serverResponse.setContentType(readFileData.getContentType(clientRequest.getFile()));
                File file = new File(ResourceConfig.WEB_ROOT, clientRequest.getFile());
                serverResponse.sendGet(clientRequest, file, readFileData);
            }

            //No support for static post files
            else if (isStaticFile(clientRequest) && clientRequest.isGet()){

                File file = new File(ResourceConfig.WEB_ROOT, clientRequest.getFile());
                serverResponse.setContentType(readFileData.getContentType(clientRequest.getFile()));
                serverResponse.sendGet(clientRequest, file, readFileData);

            }
            else if (isDynamic(clientRequest)){

                if (clientRequest.isGetOrHead()){

                    // Converts url string
                    if (clientRequest.isGet()
                            && clientRequest.getFile().startsWith("/json")
                            && clientRequest.getFile().length() > 5) {
                        serverResponse.setJson(clientRequest.getFile());
                        serverResponse.sendPostJson();

                    }

                } else if(clientRequest.isPost()) {
                    String clientBody = clientRequest.getPayloadString();
                    //Puts Json in byte[] and sends to client
                    serverResponse.setJson(JsonParser.formatSlicedUrl(clientBody));
                    serverResponse.sendPostJson();

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

    private boolean isStaticFile(ClientRequest clientRequest) {
        if (clientRequest.getFile().contains("."))  {
            return true;
        }
        else return false;
    }

    private boolean isPlugin(ClientRequest clientRequest){
        if (!isStaticFile(clientRequest) && !clientRequest.getFile().contains("?") && !clientRequest.getFile().contains("=") && !clientRequest.getFile().equals("/")) {
            return true;
        } else
            return false;
    }

    private boolean isDynamic(ClientRequest clientRequest) {
        if (!isStaticFile(clientRequest) && !(isPlugin(clientRequest))) {
            return true;
        } else
            return false;
    }
}