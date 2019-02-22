package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;

import static org.ia.util.StorageController.storage;

@Adress("v1/FindInDBPlugin")
public class FindInDBPlugin implements ImpendingInterface {
    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {
        System.out.println(storage.findFirstPerson(clientRequest.getPayloadString()));
        return serverResponse;
    }
}
