package org.ia.api;

import org.ia.util.ClientRequest;
import org.ia.util.RequestData;
import org.ia.util.ServerResponse;

public interface ImpendingInterface {

    void execute(ClientRequest clientRequest, ServerResponse serverResponse);
    //Ska returna serverrespone
}
