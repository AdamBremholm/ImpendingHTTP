package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.util.ClientRequest;
import org.ia.util.RequestData;
import org.ia.util.ServerResponse;

import java.net.Socket;

@Adress("/v1/ImpendingInterFace")
public class JsonWriter implements org.ia.api.ImpendingInterface {


    @Override
    public ServerResponse execute(ServerResponse serverResponse) {

        serverResponse.setJson("{\"name\" : \"Adam\"}");
        return serverResponse;


    }
}
