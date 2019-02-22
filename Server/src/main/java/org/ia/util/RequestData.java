package org.ia.util;

import java.io.Serializable;

//* TODO: Adam, var används den? Hur? Till vad?
// */

public class RequestData implements Serializable {

    public RequestData(String type, String value) {
        this.type = type;
        this.value = value;
    }

    private String type;
    private String value;

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
        return type + ": " + value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}



