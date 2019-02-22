package org.ia.util;

import org.ia.api.Storage;

import org.ia.Main;
import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;

import java.io.*;
import java.net.Socket;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.ServiceLoader;
import java.net.MalformedURLException;

import static org.ia.util.StorageController.storage;

public class ClientConnection implements Runnable {

    public static ArrayList<ImpendingInterface> Plugins = new ArrayList<ImpendingInterface>();

    static final boolean verbose = true;

    // Client Connection via Socket Class
    public Socket connect;
    public ClientConnection(Socket c) {
        connect = c;
    }
    ReadFileData readFileData = new ReadFileData();

    StorageController storageController = new StorageController(new MongoDB());


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


            //TODO: DB search från find-button. Hade velat göra om find till att den kollar hela formuläret och gör
            //en ny Person om det inte redan finns en. 1 Sök, 2 Skapa. Skicka genom plugins. Note: Kan inte ligga i en
            //sån if, måste hamna i rätt /, så att den inte bara printar fältets payload som
            // Json till skärmen.
            if (clientRequest.getFile().equals("/") && clientRequest.isPost()) {
//                System.out.println(clientRequest.payload.toString());

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

            //Hämtar statisk fil om fil innehåller .
            else if (isStaticFile(clientRequest) && !clientRequest.isPost()){

                File file = new File(ResourceConfig.WEB_ROOT, clientRequest.getFile());
                serverResponse.setContentType(readFileData.getContentType(clientRequest.getFile()));
                if (clientRequest.isGet())
                serverResponse.sendGet(file);
                else if (clientRequest.isHead()) //Skicka bara Headers tillbaka
                    serverResponse.sendHead(clientRequest, file, readFileData);
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
                    }
                    else { //Send not implemented if unsupported URL.
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

                for (ImpendingInterface impendingInterfaceImplementation : loader) {
                    if (impendingInterfaceImplementation.getClass().getAnnotation(Adress.class).value().equalsIgnoreCase(clientRequest.getFile())) {
                        impendingInterfaceImplementation.execute(clientRequest, serverResponse);
                    }

                }

            }

            //Skicka file not found
            else {

                readFileData.fileNotFound(serverResponse.getOut(), serverResponse.getDataOut(), clientRequest.getFile());
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