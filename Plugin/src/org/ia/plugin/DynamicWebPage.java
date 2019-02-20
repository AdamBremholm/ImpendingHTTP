package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;


@Adress("/v1/ImpendingInterFace")
public class DynamicWebPage implements org.ia.api.ImpendingInterface {


    @Override
    public void execute(ClientRequest clientRequest, ServerResponse serverResponse) {

    }
}
