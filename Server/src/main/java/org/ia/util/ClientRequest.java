package org.ia.util;

public class ClientRequest {

    String method;
    String file = null;
    String accept;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    String contentType;
    String body = "";

    public ClientRequest(String method, String file, String accept, String body) {
        this.method = method;
        this.file = file;
        this.accept = accept;
        this.body = body;
    }

    public ClientRequest() {

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

    public boolean isGet() {
        if (method.equals("GET")) {
            return true;
        } else return false;
    }

    public boolean isHead() {
        if (method.equals("HEAD")) {
            return true;
        } else return false;
    }

    public boolean isGetOrHead() {
        return (isHead() || isGet());
    }

    public boolean isGetOrHeadOrPost() { return isGetOrHead() || isPost(); }

    public boolean isPost() {
        if (method.equals("POST")) {
            return true;
        } else return false;
    }
}
