package org.ia.util;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ServiceLoaderUtil {

    public static URLClassLoader createClassLoader(String fileLocation) {
        File loc = new File(fileLocation);

        File[] flist = loc.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getPath().toLowerCase().endsWith(".jar");
            }
        });
        URL[] urls = new URL[flist.length];
        for (int i = 0; i < flist.length; i++) {
            try {
                urls[i] = flist[i].toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return new URLClassLoader(urls);
    }

}
