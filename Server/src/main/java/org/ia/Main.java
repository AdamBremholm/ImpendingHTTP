package org.ia;

import org.ia.api.Adress;
import org.ia.api.ImpendingInterface;
import org.ia.util.JavaHTTPServer;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;


public class Main {

    public static void main( String[] args )
    {

        Main main = new Main();
        main.run(args);

        //Start our Impending server


    }

    private URLClassLoader createClassLoader(String fileLocation) {
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


    private void run(String[] args) {

        String pluginFolder = "";
        //if no arguments set to current directory
        if (args.length==0) {
            pluginFolder = "./";
        } else {
            pluginFolder = args[0];
        }



        URLClassLoader ucl = createClassLoader(pluginFolder);

        ServiceLoader<ImpendingInterface> loader =
                ServiceLoader.load(ImpendingInterface.class, ucl);

        for (ImpendingInterface greetings : loader) {
            if (greetings.getClass().getAnnotation(Adress.class).value().equals("/v1/ImpendingInterFace"))
                greetings.execute();
        }


        JavaHTTPServer.startServer();


    }



}
