package com.oodrive.omnikles.depotclient;

import com.oodrive.omnikles.depotclient.pojo.Configuration;
import com.oodrive.omnikles.depotclient.service.SslConnexionService;
import com.oodrive.omnikles.depotclient.swing.window.IntroWindow;
import com.oodrive.omnikles.depotclient.swing.window.TestWindow;

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

    public static Properties textProperties = new Properties();

    public CryptoDoc() throws IOException {
        SslConnexionService sc = new SslConnexionService();
        System.out.println("WebStart CryptoDoc - version : " + getAppVersion());
        if(Configuration.parameters.get("language") == null || Configuration.parameters.get("language").isEmpty()) {
            Configuration.parameters.put("language", "fr");
        }
        initTextes(Configuration.parameters.get("language"));
    }

    public static void main(String[] args) throws InvalidKeyException, javax.security.cert.CertificateException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, UnsupportedLookAndFeelException {
        CryptoDoc cryptoDoc = new CryptoDoc();
//        TestWindow test = new TestWindow();
//        test.gossl();
        Configuration.initParameters(args);

        System.out.println(Configuration.activFolder);

        if(Configuration.parameters.get("action").equals("depot")) {
            depot();
        }

        if(Configuration.parameters.get("action").equals("decrypt")){
            openDepot();
        }
        if(Configuration.parameters.get("action").equals("test")){
            new TestWindow();
        }

    }

    private static void openDepot() throws MalformedURLException, FileNotFoundException {
//        mainWindow.init();
//        mainWindow.setVisible(true);
    }

    private static void depot() throws IOException {
        new IntroWindow();
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
        URL url = this.getClass().getResource("/texts_" + language + ".properties");
        if(url != null && url.getFile() != null) {
            textProperties.load(new InputStreamReader((url.openStream()),"UTF-8"));
        }
    }
}
