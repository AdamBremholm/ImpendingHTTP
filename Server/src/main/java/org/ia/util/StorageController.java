package org.ia.util;

import org.ia.api.Storage;

public class StorageController {

    public static Storage storage;

    public StorageController(Storage storage) {
        this.storage = storage;
    }

    public Storage getStorage() {
        return storage;
    }
}
