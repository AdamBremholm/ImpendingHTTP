package org.ia.util;

import database.ListStorage;
import database.MongoDB;
import org.ia.Main;
import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;

import java.io.*;
import java.net.Socket;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.ServiceLoader;

import static org.ia.util.StorageController.storage;

//* Handles first contact with connection.
// In short, redirects based on HTTP-method and URI.
// A.k.a if/else-hell.
// */

public class ClientConnection implements Runnable {

    public static ArrayList<ImpendingInterface> Plugins = new ArrayList<ImpendingInterface>();

    static final boolean verbose = true;

    // Client Connection via Socket Class
    public Socket connect;
    public ClientConnection(Socket c) {
        connect = c;
    }
    ReadFileData readFileData = new ReadFileData();

    @Override
    public void run() {

        //Loads the right database.
        StorageController storageController;
        if (ResourceConfig.STORAGE_METHOD.equals("list")) {
            storageController = new StorageController(new ListStorage());
        } else {
            storageController = new StorageController(new MongoDB());
        }

        //Creates request and response objects from socket connection.
        ClientRequest clientRequest = new ClientRequest(connect);
        ServerResponse serverResponse = new ServerResponse(connect, readFileData);

        try {
            //In, out and dataOut
            clientRequest.initReaders();
            serverResponse.initWriters();

            //Saves the request to database. Important for /v1/statistic.
            storage.addRequest(clientRequest);

            if (clientRequest.getFile().equals("/") && clientRequest.isPost()) {
            }
            //Skickar 501 om man skickar något annat än get head eller post
            if (!clientRequest.isGetOrHeadOrPost()) {
                if (verbose) {
                    System.out.println("501 Not Implemented : " + clientRequest.getMethod() + " method.");
                }
                serverResponse.sendNotImplemented(clientRequest);

            }
            //Skickar startstidan om man inte skriver något mer
            else if (clientRequest.getFile().equals("/") && !clientRequest.bodyExists()) {
                clientRequest.setFile("/" + ResourceConfig.DEFAULT_FILE);
                serverResponse.setContentType(readFileData.getContentType(clientRequest.getFile()));
                File file = new File(ResourceConfig.WEB_ROOT, clientRequest.getFile());
                serverResponse.sendGet(file);
            }
            //Hämtar statisk fil om fil innehåller.
            else if (isStaticFile(clientRequest) && !clientRequest.isPost()){

                File file = new File(ResourceConfig.WEB_ROOT, clientRequest.getFile());
                serverResponse.setContentType(readFileData.getContentType(clientRequest.getFile()));
                if (clientRequest.isGet())
                serverResponse.sendGet(file);
                else if (clientRequest.isHead()) //Skicka bara Headers tillbaka
                    serverResponse.sendHead(file);
            }
            //Dynamisk
            else if (isDynamic(clientRequest)){
                if (clientRequest.isGetOrHead()){
                    // Converts url string
                    if (clientRequest.isGet()
                            && clientRequest.getFile().startsWith("/json")
                            && clientRequest.getFile().length() > 5) {
                        serverResponse.setJson(clientRequest.getFile());
                        serverResponse.sendPostJson();
                    } else if (clientRequest.isHead()
                            && clientRequest.getFile().startsWith("/json")
                            && clientRequest.getFile().length() > 5) {
                        serverResponse.setJson(clientRequest.getFile());
                        serverResponse.sendHeadJson();
                        //Skicka bara Headers tillbaka
                    } else { //Send not implemented if unsupported URL.
                        serverResponse.sendNotImplemented(clientRequest);
                    }
                } else if(clientRequest.isPost()) {
                    if (clientRequest.bodyExists()) {
                        String clientBody = clientRequest.getPayloadString();
                        //Puts Json in byte[] and sends to client
                        serverResponse.setJson(JsonParser.formatSlicedUrl(clientBody));
                        serverResponse.sendPostJson();
                    } else if (clientRequest.getFile().startsWith("/json") //Om post men utan body och json i url
                            && clientRequest.getFile().length() > 5) {
                        serverResponse.setJson(clientRequest.getFile());
                        serverResponse.sendPostJson();
                    } else { //Om ingen body och ingen jsonurl
                        serverResponse.sendNotImplemented(clientRequest);
                    }
                }
            }
            //Plugin
            else if (isPlugin(clientRequest)) {
                URLClassLoader ucl = ServiceLoaderUtil.createClassLoader(Main.getPluginFolder());

                ServiceLoader<ImpendingInterface> loader =
                        ServiceLoader.load(ImpendingInterface.class, ucl);
                int i = 0;
                for (ImpendingInterface impendingInterfaceImplementation : loader) {
                    if (impendingInterfaceImplementation.getClass().getAnnotation(Adress.class).value().equalsIgnoreCase(clientRequest.getFile())) {
                        impendingInterfaceImplementation.execute(clientRequest, serverResponse);
                        i++;
                    }
                }
                if (i==0) {
                    //Om inget plugin hittas, 404 file not found.
                    clientRequest.setFile("/" + ResourceConfig.FILE_NOT_FOUND);
                    serverResponse.setContentType(readFileData.getContentType(clientRequest.getFile()));
                    File file = new File(ResourceConfig.WEB_ROOT, clientRequest.getFile());
                    serverResponse.sendGet(file);
                }
            }
            //Skicka file not found
            else {
                readFileData.fileNotFound(serverResponse.getOut(), serverResponse.getDataOut(), clientRequest.getFile());
            }
        } catch (FileNotFoundException fnfe) {
            try {
                readFileData.fileNotFound(serverResponse.getOut(), serverResponse.getDataOut(), clientRequest.getFile());
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