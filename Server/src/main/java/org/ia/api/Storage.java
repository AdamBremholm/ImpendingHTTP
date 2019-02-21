package org.ia.api;

import org.ia.util.ClientRequest;

public interface Storage {

    void addRequest(ClientRequest clientRequest);
    String getRequests();
    int getRequestCount();
    void close();


}
