package ia.util;

public class Request {

    String method, url, accept;
    String body = "";

    public Request(String method, String url, String accept, String body) {
        this.method = method;
        this.url = url;
        this.accept = accept;
        this.body = body;
    }

    public Request() {

    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public String getURL() {
        return url;
    }

    public Boolean isGet() {
        if (method.equals("GET")) {
            return true;
        } else return false;
    }

    public Boolean isHead() {
        if (method.equals("HEAD")) {
            return true;
        } else return false;
    }

    public Boolean isGetOrHead() {
        return (isHead() || isGet());
    }
}
