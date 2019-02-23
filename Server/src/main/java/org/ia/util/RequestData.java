package org.ia.util;

import java.io.Serializable;

/* This is how each each item of the request that is send to the server. Put in requestDataList (Array list) to represent the headers for each request sent to server.
The requests are later saved in our Storage.
Same functionality as a Hash Map but ordered.
 */

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



