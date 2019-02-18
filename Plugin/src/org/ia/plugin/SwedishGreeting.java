package org.ia.plugin;

import org.ia.api.Adress;

@Adress("/v1/ImpendingInterFace")
public class SwedishGreeting implements org.ia.api.ImpendingInterface {
    @Override
    public void execute() {

        System.out.println("Tjena Tjena från Sverige");

    }
}
