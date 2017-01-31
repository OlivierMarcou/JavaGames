package com.oodrive.omnikles.depotclient;

import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.swing.window.CloseWindow;
import com.oodrive.omnikles.depotclient.swing.window.MainWindow;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.Properties;


/**
 * Created by olivier on 16/09/16.
 */


public class CryptoDoc {

    private static CloseWindow closeWindow;
    private static MainWindow mainWindow;

    public static void main(String[] args) throws InvalidKeyException, javax.security.cert.CertificateException, IOException {
        CryptoDoc cryptoDoc = new CryptoDoc();
        System.out.println("WebStart CryptoDoc - version : " + cryptoDoc.getAppVersion());
        CryptoDocConfiguration.initParameters(args);

        mainWindow = new MainWindow();
        closeWindow = new CloseWindow();
        System.out.println(CryptoDocConfiguration.activFolder);
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

    public String getAppVersion() throws IOException{
        String versionString = null;
        Properties mainProperties = new Properties();
        URL url = this.getClass().getResource("/cryptodoc.properties");
        if(url != null && url.getFile() != null) {
            mainProperties.load(url.openStream());
            versionString = mainProperties.getProperty("build.version");
            return versionString;
        }
        return null;
    }
}
