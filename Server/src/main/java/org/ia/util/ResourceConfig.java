package org.ia.util;

import java.io.File;

public class ResourceConfig {

    ClassLoader classLoader = getClass().getClassLoader();
    static final File WEB_ROOT = new File("./webroot");

    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";


}
