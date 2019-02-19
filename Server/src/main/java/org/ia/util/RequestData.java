package org.ia.util;

public class RequestData {

    public RequestData(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public RequestData() {
    }

    String type;
    String value;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return type + ":" + value;
    }
}



