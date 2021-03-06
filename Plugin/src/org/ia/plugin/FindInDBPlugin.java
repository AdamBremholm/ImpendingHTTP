package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;

import java.io.IOException;

import static org.ia.util.StorageController.storage;

//* Returns the first person that matches form input from the database.
// Reads POST payload, makes serverResponse send it as POST payload.
// */

@Adress("/v1/Person")
public class FindInDBPlugin implements ImpendingInterface {
    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {
        String searchResult = storage.findFirstPerson(clientRequest.getPayloadString());
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
