package org.ia.api;

import org.ia.util.ClientRequest;
import org.ia.util.Person;
import org.json.simple.JSONArray;


public interface Storage {

    void addRequest(ClientRequest clientRequest);
    String getRequests();
    int getRequestCount();
    void close();
    void addPerson(Person person);
    String findFirstPerson(String searchParam);
    JSONArray findAllPersons();
    int getPersonCount();


}
