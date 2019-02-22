package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;
import org.ia.api.ImpendingInterface;

@Adress("/v1/UpsertPlugin")
public class UpsertPlugin implements ImpendingInterface {
    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {
        System.out.println("I'M CALLING FROM UPSERTPLUGIN");
        return serverResponse;
    }
}
