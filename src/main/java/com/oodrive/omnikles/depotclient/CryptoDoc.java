package com.oodrive.omnikles.depotclient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Properties;


/**
 * Created by olivier on 16/09/16.
 */


public class CryptoDoc {

    private static CloseWindow closeWindow = new CloseWindow();
    private static MainWindow mainWindow = new MainWindow();

    public static void main(String[] args) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        System.out.println("WebStart CryptoDoc - version : " + getAppVersion());
        System.out.println(System.getProperty("user.home"));
        HashMap<String, String> parameters = new HashMap<>();

        String[] keyValue = new String[2];
        for (String parameter: args ){
            int indexEqual = parameter.trim().indexOf("=");
            keyValue[0] = parameter.substring(0, indexEqual);
            keyValue[0].replaceFirst("-","");
            keyValue[1] = parameter.substring(indexEqual+1);
            System.out.println(keyValue[0] + " " + keyValue[1]);
            parameters.put(keyValue[0], keyValue[1]);
        }
        if(parameters.get("action").equals("depot")) {
            depot(parameters);
        }

        if(parameters.get("action").equals("decrypt")){
            openDepot(parameters);
        }
    }

    private static void openDepot(HashMap<String, String> parameters) throws MalformedURLException, FileNotFoundException {
        mainWindow.init(parameters);
        mainWindow.setVisible(true);
    }

    private static void depot(HashMap<String, String> parameters) throws IOException {
        mainWindow.init(parameters);
        mainWindow.setVisible(false);
        mainWindow.depot();
        closeWindow.setVisible(true);
    }

    public static String getAppVersion() throws IOException{

        String versionString = null;
        Properties mainProperties = new Properties();
        FileInputStream file;
        URL url =  ClassLoader.getSystemResource("cryptodoc.properties");
        if(url != null && url.getFile() != null) {
            mainProperties.load(url.openStream());
            versionString = mainProperties.getProperty("build.version");
            return versionString;
        }
        return null;
    }

}
