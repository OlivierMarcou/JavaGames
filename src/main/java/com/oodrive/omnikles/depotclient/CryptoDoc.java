package com.oodrive.omnikles.depotclient;

import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.swing.window.CloseWindow;
import com.oodrive.omnikles.depotclient.swing.window.IntroWindow;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.Properties;


/**
 * Created by olivier on 16/09/16.
 */


public class CryptoDoc {

    private static CloseWindow closeWindow;
//    private static MainWindow mainWindow;
    public static Properties textProperties = new Properties();

    public CryptoDoc() throws IOException {
        System.out.println("WebStart CryptoDoc - version : " + getAppVersion());
        if(CryptoDocConfiguration.parameters.get("language") == null || CryptoDocConfiguration.parameters.get("language").isEmpty()) {
            CryptoDocConfiguration.parameters.put("language", "fr");
        }
        initTextes(CryptoDocConfiguration.parameters.get("language"));
    }

    public static void main(String[] args) throws InvalidKeyException, javax.security.cert.CertificateException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, UnsupportedLookAndFeelException {
        CryptoDoc cryptoDoc = new CryptoDoc();

        CryptoDocConfiguration.initParameters(args);

//        mainWindow = new MainWindow();
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
//        mainWindow.init();
//        mainWindow.setVisible(true);
    }

    private static void depot() throws IOException {
        IntroWindow introWindow = new IntroWindow();
        //        mainWindow.init();
//        mainWindow.setVisible(false);
//        mainWindow.depot();
        //closeWindow.setVisible(true);
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



    public void initTextes(String language) throws IOException{
        String versionString = null;
        URL url = this.getClass().getResource("/texts_" + language + ".properties");
        if(url != null && url.getFile() != null) {
            textProperties.load(new InputStreamReader((url.openStream()),"UTF-8"));
        }
    }
}
