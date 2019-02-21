package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;


@Adress("/v1/DynamicWebPage")
public class DynamicWebPage implements org.ia.api.ImpendingInterface {


    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {
        return serverResponse;
    }
}
