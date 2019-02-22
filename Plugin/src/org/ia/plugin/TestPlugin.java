package org.ia.plugin;

import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;
import org.ia.util.ClientRequest;
import org.ia.util.ServerResponse;


@Adress("/v1/TestPlugin")
public class TestPlugin implements ImpendingInterface {
    @Override
    public ServerResponse execute(ClientRequest clientRequest, ServerResponse serverResponse) {
        System.out.println("hej!");
        return serverResponse;
    }
}
