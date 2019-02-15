package ia.org.util;

public class Request {

    String method, file, accept;
    String body = "";

    public Request(String method, String file, String accept, String body) {
        this.method = method;
        this.file = file;
        this.accept = accept;
        this.body = body;
    }

    public Request() {

    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMethod() {
        return method;
    }

    public String getFile() { return file;    }

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
