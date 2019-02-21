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

    public static String getPluginFolder() {
        return pluginFolder;
    }


    private static String pluginFolder;

    public static void main( String[] args )
    {

        Main main = new Main();
        main.run(args);

        //Start our Impending server


    }




    private void run(String[] args) {

         pluginFolder = "";
        //if no arguments set to current directory
        if (args.length==0) {
            pluginFolder = "./";
        } else {
            pluginFolder = args[0];
        }



        JavaHTTPServer.startServer();


    }



}
