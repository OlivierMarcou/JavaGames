package com.oodrive.omnikles.depotclient;

import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.swing.window.CloseWindow;
import com.oodrive.omnikles.depotclient.swing.window.MainWindow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.Properties;


/**
 * Created by olivier on 16/09/16.
 */


public class CryptoDoc {

    private static CloseWindow closeWindow = new CloseWindow();
    private static MainWindow mainWindow = new MainWindow();

    public static void main(String[] args) throws InvalidKeyException, javax.security.cert.CertificateException, IOException {
        System.out.println("WebStart CryptoDoc - version : " + getAppVersion());
        System.out.println(System.getProperty("user.home"));
        CryptoDocConfiguration.initParameters(args);
        if(CryptoDocConfiguration.parameters.get("action").equals("depot")) {
            depot();
        }

        if(CryptoDocConfiguration.parameters.get("action").equals("decrypt")){
            openDepot();
        }

    }

    private static void openDepot() throws MalformedURLException, FileNotFoundException {
        mainWindow.init();
        mainWindow.setVisible(true);
    }

    private static void depot() throws IOException {
        mainWindow.init();
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
