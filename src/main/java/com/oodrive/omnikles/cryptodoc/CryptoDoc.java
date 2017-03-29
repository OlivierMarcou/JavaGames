package com.oodrive.omnikles.cryptodoc;

import com.oodrive.omnikles.cryptodoc.deposit.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.swing.window.IntroWindow;
import com.oodrive.omnikles.cryptodoc.swing.window.LogWindow;
import com.oodrive.omnikles.cryptodoc.swing.window.TestWindow;

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

    public CryptoDoc(String[] args) throws IOException {
        Configuration.initParameters(args);
        if(Configuration.debug){
            new LogWindow();
        }
        System.out.println("WebStart CryptoDoc - version : " + getAppVersion());
        if(Configuration.parameters.get("language") == null || Configuration.parameters.get("language").isEmpty()) {
            Configuration.parameters.put("language", "fr");
        }
        initTextes(Configuration.parameters.get("language"));
    }

    public static void main(String[] args) throws InvalidKeyException, javax.security.cert.CertificateException,
            IOException, ClassNotFoundException, IllegalAccessException,
            InstantiationException, UnsupportedLookAndFeelException {
        CryptoDoc cryptoDoc = new CryptoDoc(args);
        System.out.println(Configuration.activFolder);

        if(Configuration.parameters.get("action").equals("depot")) {
            depot();
        }

        if(Configuration.parameters.get("action").equals("decrypt")){
            openDepot();
        }
        if(Configuration.parameters.get("action").equals("test")){
            TestWindow test = new TestWindow();
            changeLookAndFeel(2, test);
        }

        if(Configuration.parameters.get("action").equals("class")){
            Configuration.parameters.put("language","fr");
            Configuration.parameters.put("titleProcedure","titleProcedure");
            Configuration.parameters.put("organismName","organismName");
            Configuration.parameters.put("urlCryptedFile","http://localhost/");
            Configuration.parameters.put("sessionid","111111111");
            Configuration.parameters.put("filename","test.pdf");
            Configuration.parameters.put("urlCertificat","http://localhost/");
            Configuration.parameters.put("urlDepot","http://localhost/");
            JFrame test = (JFrame) Class.forName("com.oodrive.omnikles.cryptodoc.swing.window."+Configuration.parameters.get("class")).newInstance();
            test.setVisible(true);
        }

    }

    public static void changeLookAndFeel(int index, JFrame frame){
        try {
            String name = UIManager.getInstalledLookAndFeels()[index].getClassName();
            UIManager.setLookAndFeel(name);
            SwingUtilities.updateComponentTreeUI(frame);
            frame.pack();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            Configuration.version = mainProperties.getProperty("actual.version");
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