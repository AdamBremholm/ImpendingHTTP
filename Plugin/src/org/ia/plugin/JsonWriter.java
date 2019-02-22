package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;
import java.io.IOException;


@Adress("/v1/JsonWriter")
public class JsonWriter implements org.ia.api.ImpendingInterface {

    //* Sends name=Adam with POST body.
    // */

    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {


        serverResponse.setJson("?name=Adam");
        try {
            serverResponse.sendPostJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;


    }
}
