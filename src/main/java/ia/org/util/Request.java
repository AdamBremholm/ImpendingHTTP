package ia.org.util;

public class Request {


    public Request(String method, String fileRequested) {
        this.method = method;
        this.fileRequested = fileRequested;
    }

    public Request(String method, String fileRequested, String contentLength, String postBody) {
        this(method, fileRequested);
        this.contentLength = contentLength;
        this.postBody = postBody;
    }

    public Request() {
        this("","","","");
    }

    public String method;
    public String fileRequested;
    public String contentLength;
    public String postBody;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFileRequested() {
        return fileRequested;
    }

    public void setFileRequested(String fileRequested) {
        this.fileRequested = fileRequested;
    }

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }
}
