package org.ia.util;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;

// The tutorial can be found just here on the SSaurel's Blog :
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// Each Client Connection will be managed in a dedicated Thread
public class JavaHTTPServer  {

    ClientConnection clientConnection;
    /*** Sätter resources root till rätt mapp.*/
    // port to listen connection
    static final int PORT = 8083;

    // verbose mode
    static final boolean verbose = true;


    public static void startServer() {

        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

            // we listen until user halts server execution
            while (true) {
                ClientConnection client = new ClientConnection(serverConnect.accept());

                if (verbose) {
                    System.out.println("Connection opened. (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                Thread thread = new Thread(client);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }

    }

}
