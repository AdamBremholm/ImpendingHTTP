package ia.org.util;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

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
        ClientRequest clientRequest = new ClientRequest();

        // we manage our particular client connection
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;

        try {
            // we read characters from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            // we get character output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();
            // we parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);
            clientRequest.setMethod(parse.nextToken().toUpperCase()); // we get the HTTP method of the client
            // we get file requested
            clientRequest.setFile(parse.nextToken().toLowerCase());

            // we support only GET and HEAD methods, we check
            if (!clientRequest.isGetOrHeadOrPost()) {
                if (verbose) {
                    System.out.println("501 Not Implemented : " + clientRequest.getMethod() + " method.");
                }
                // we return the not supported file to the client
                File file = new File(ResourceConfig.WEB_ROOT, ResourceConfig.METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();
                clientRequest.setContentType("text/html");
                //read content to return to client
                byte[] fileData = readFileData.readFileData(file, fileLength);

                // we send HTTP Headers with data to client
                out.println("HTTP/1.1 501 Not Implemented");
                out.println("Server: Java HTTP Server from SSaurel : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + clientRequest.getContentType());
                out.println("Content-length: " + fileLength);
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer
                // file
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

            }

            /**
             * Metoden som checkar för POST. Läser nästa linje tills det är tom rad. sen kör den vidare och appendar varje byte (omgjort till char) som in läser tills det inte finns mer matrial kvar.
             */
            else if (clientRequest.isPost()) {

                String headerLine = null;
                while((headerLine = in.readLine()).length() != 0){
                    System.out.println(headerLine);
                }

                StringBuilder payload = new StringBuilder();
                while(in.ready()){
                    payload.append((char) in.read());
                }

                System.out.println("Payload data is: "+payload.toString());

                if (clientRequest.getFile().endsWith("/")) {
                    clientRequest.setFile(clientRequest + ResourceConfig.DEFAULT_FILE);
                }

                File file = new File(ResourceConfig.WEB_ROOT, clientRequest.getFile());
                int fileLength = (int) file.length();
                String content = readFileData.getContentType(clientRequest.getFile());

                if (clientRequest.isPost()) { //TODO: Detta är bara för att skicka tillbaka ett response till browsern. Kan behöva ändras för post. Just nu är det samma som GET.
                    byte[] fileData = readFileData.readFileData(file, fileLength);

                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    out.println("Content-length: " + fileLength);
                    out.println(); // blank line between headers and content, very important !
                    out.flush(); // flush character output stream buffer

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }

                if (verbose) {
                    System.out.println("File " + clientRequest.getFile() + " of type " + content + " returned");
                }


            }


             else {
                // GET or HEAD method
                if (clientRequest.getFile().endsWith("/")) {
                    clientRequest.setFile(clientRequest.getFile() + ResourceConfig.DEFAULT_FILE);
                }


                File file = new File(ResourceConfig.WEB_ROOT, clientRequest.getFile());
                int fileLength = (int) file.length();
                clientRequest.setContentType(readFileData.getContentType(clientRequest.getFile()));

                if (clientRequest.isGet()) { // GET method so we return content
                    byte[] fileData = readFileData.readFileData(file, fileLength);

                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server from SSaurel : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + clientRequest.getContentType());
                    out.println("Content-length: " + fileLength);
                    out.println(); // blank line between headers and content, very important !
                    out.flush(); // flush character output stream buffer

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }

                if (verbose) {
                    System.out.println("File " + clientRequest.getFile() + " of type " + clientRequest.getContentType() + " returned");
                }

            }

        } catch (FileNotFoundException fnfe) {
            try {
                readFileData.fileNotFound(out, dataOut, clientRequest.getFile());
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
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