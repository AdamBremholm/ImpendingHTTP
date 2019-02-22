package org.ia.util;

import org.ia.api.Storage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ListStorage implements Storage {

    List<ClientRequest> requests = new ArrayList<>();
    List<Person> persons = new ArrayList<>();
    public ListStorage(){
        //Läs in sparade kunder från fil
        loadFromFile();
    }

    public void addRequest(ClientRequest clientRequest){
        requests.add(clientRequest);
        saveToFile();
    }

    @Override
    public String getRequests() {
        return requests.toString();
    }

    @Override
    public int getRequestCount() {
        return requests.size();
    }

    public void close(){
        saveToFile();
    }

    @Override
    public void addPerson(Person person) {
        persons.add(person);
    }

    @Override
    public String findFirstPerson(String searchParam) {

        for (Person person: persons) {
            if (searchParam.equalsIgnoreCase(searchParam)) {
                return person.toString();
            }
        }
        return searchParam + " not found.";
    }

    @Override
    public ArrayList<String> findAllPersons() {
        return null;
    }

    @Override
    public int getPersonCount() {
        return persons.size();
    }

    @Override
    public JSONArray getRequestsAsJsonArray() {
        return null;
    }

    @Override
    public JSONObject getRequestsAsJsonObject() {
        return null;
    }

    private void loadFromFile() {
        String path = System.getProperty("user.home")
                + File.separator + "Documents"
                + File.separator + "customers.bin";
        File file = new File(path);

        //Load from file, run this code first in your program on start.
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            requests = (ArrayList<ClientRequest>) in.readObject();
        } catch (FileNotFoundException e) {
            //On first start you will end up here. No file available.
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }
    }

    private void saveToFile() {
        String path = System.getProperty("user.home")
                + File.separator + "Documents"
                + File.separator + "requests.bin";
        File file = new File(path);

        //Save object to file, run before closing the program
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            out.writeObject(requests);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
