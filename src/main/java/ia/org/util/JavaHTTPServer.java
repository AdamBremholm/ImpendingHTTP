package ia.org.util;

import ia.util.Request;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

// The tutorial can be found just here on the SSaurel's Blog :
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// Each Client Connection will be managed in a dedicated Thread
public class JavaHTTPServer  {

    ClientConnection clientConnection;
    /*** Sätter resources root till rätt mapp.*/

    final File WEB_ROOT = new File("./webroot/");

    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";
    // port to listen connection
    static final int PORT = 8080;

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
                    System.out.println("Connecton opened. (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                Thread thread = new Thread(client);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }

    }


    public Request makeRequest (BufferedReader in) throws IOException {

        Request request = new Request();

        in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
        // we get character output stream to client (for headers)
        String input = in.readLine();
        // we parse the request with a string tokenizer
        StringTokenizer parse = new StringTokenizer(input);
        request.setMethod(parse.nextToken().toUpperCase());
        request.setURL(parse.nextToken().toLowerCase());

        return request;
    }


    @Override
    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;

        Request request = null;

        try {
            out = new PrintWriter(connect.getOutputStream());
            dataOut = new BufferedOutputStream(connect.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            request = makeRequest(in);

            if (!request.isGetOrHead()) {
                if (verbose) {
                    System.out.println("501 Not Implemented : " + request.getMethod() + " method.");
                }

                // we return the not supported file to the client
                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();
                String contentMimeType = "text/html";
                //read content to return to client
                byte[] fileData = readFileData(file, fileLength);

                // we send HTTP Headers with data to client
                out.println("HTTP/1.1 501 Not Implemented");
                out.println("Server: Java HTTP Server from SSaurel : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + contentMimeType);
                out.println("Content-length: " + fileLength);
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer
                // file
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

            } else {
                // GET or HEAD method
                if (request.getURL().endsWith("/")) {
                    request.setURL(request.getURL()+ DEFAULT_FILE);
                }

                File file = new File(WEB_ROOT, request.getURL());
                int fileLength = (int) file.length();
                String content = getContentType(request.getURL());

                if (request.isGet()) { // GET method so we return content
                    byte[] fileData = readFileData(file, fileLength);

                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server from SSaurel : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    out.println("Content-length: " + fileLength);
                    out.println(); // blank line between headers and content, very important !
                    out.flush(); // flush character output stream buffer

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }

                if (verbose) {
                    System.out.println("File " + request.getURL() + " of type " + content + " returned");
                }
            }

        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, request.getURL());
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

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }
*/
/*
    // return supported MIME Types
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else if (fileRequested.endsWith(".css")) {
            return "text/css";
        }
        else if (fileRequested.endsWith(".js")) {
            return "text/javascript";
        }
        else if (fileRequested.endsWith(".pdf")) {
            return "application/pdf";

        }
        else if (fileRequested.endsWith(".png")) {
            return "image/png";

        }
        else if (fileRequested.endsWith(".jpg") || fileRequested.endsWith(".jpeg")) {
            return "image/jpeg";

        }

        else
            return "text/plain";
    }
*/

/*
    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: Java HTTP Server from SSaurel : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose) {
            System.out.println("File " + fileRequested + " not found");
        }
    }
    */

}
