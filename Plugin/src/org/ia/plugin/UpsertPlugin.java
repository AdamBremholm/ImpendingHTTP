package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;
import org.ia.util.ClientRequest;
import org.ia.util.JsonParser;
import org.ia.util.Person;
import org.ia.util.ServerResponse;
import org.json.simple.JSONObject;

import static org.ia.util.StorageController.storage;

@Adress("/v1/Person")
public class UpsertPlugin implements ImpendingInterface {
    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {
        Person person = new Person();
        JSONObject obj = JsonParser.formatSlicedUrl(clientRequest.getPayloadString());
        person.setName(obj.get("name").toString());
        person.setAdress(obj.get("address").toString());
        person.setDateOfBirth(obj.get("dateOfBirth").toString());

        storage.addPerson(person);

        return serverResponse;
    }
}
