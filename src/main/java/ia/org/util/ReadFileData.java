package ia.org.util;

import java.io.*;
import java.util.Date;

public class ReadFileData {

    private static boolean verbose = true;

    ResourceConfig resourceConfig;

     byte[] readFileData(File file, int fileLength) throws IOException {
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


    void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(ResourceConfig.WEB_ROOT, ResourceConfig.FILE_NOT_FOUND);
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

}
