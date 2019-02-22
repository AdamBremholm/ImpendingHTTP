package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;

import java.io.IOException;

import static org.ia.util.StorageController.storage;

@Adress("/v1/Person")
public class FindInDBPlugin implements ImpendingInterface {
    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {
        String searchResult = storage.findFirstPerson(clientRequest.getPayloadString());
        System.out.println(storage.findFirstPerson(clientRequest.getPayloadString()));
        byte[] jsonbytes = searchResult.getBytes();
        serverResponse.setJsonBytes(jsonbytes);
        try {
            serverResponse.sendPostJson();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }
}
