package com.oodrive.omnikles.cryptodoc;

import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.ProxyConfig;
import com.oodrive.omnikles.cryptodoc.swing.window.IntroWindow;
import com.oodrive.omnikles.cryptodoc.swing.window.LogWindow;
import com.oodrive.omnikles.cryptodoc.swing.window.OpenReceivership;
import com.oodrive.omnikles.cryptodoc.swing.window.TestWindow;
import com.oodrive.omnikles.cryptodoc.utils.Logs;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.util.Properties;


/**
 * Created by olivier on 16/09/16.
 */


public class CryptoDoc {

    public static Properties textProperties = new Properties();
    public static Properties contextProperties = new Properties();

    public CryptoDoc(String[] args) throws IOException {
        Configuration.initParameters(args);
        if(Configuration.debug){
            new LogWindow();
        }
        initContext();
        Logs.sp("WebStart CryptoDoc - version : " + contextProperties.getProperty("build.version"));
        if(Configuration.parameters.get("language") == null || Configuration.parameters.get("language").isEmpty()) {
            Configuration.parameters.put("language", "fr");
        }
        initTextes(Configuration.parameters.get("language"));
        loadLocalConfiguration();

    }

    public static void main(String[] args) throws InvalidKeyException, javax.security.cert.CertificateException,
            IOException, ClassNotFoundException, IllegalAccessException,
            InstantiationException, UnsupportedLookAndFeelException {
        CryptoDoc cryptoDoc = new CryptoDoc(args);
        Logs.sp(Configuration.activFolder);

        if(Configuration.parameters.get("action").equals("depot") || Configuration.parameters.get("action").equals("depotMarches")) {
            if(Configuration.parameters.get("action").equals("depotMarches"))
                Configuration.isOkMarches=true;
            changeLookAndFeel(3, new IntroWindow());
        }

        if(Configuration.parameters.get("action").equals("decrypt") || Configuration.parameters.get("action").equals("decryptMarches")){
            OpenReceivership openReceivership = new OpenReceivership();
            if(Configuration.parameters.get("action").equals("decryptMarches")) {
                Configuration.isOkMarches=true;
            }
            openReceivership.setVisible(true);

            changeLookAndFeel(3, openReceivership);
        }

        if(Configuration.parameters.get("action").equals("test")){
            TestWindow test = new TestWindow();
            changeLookAndFeel(0, test);
        }

        if(Configuration.parameters.get("action").equals("easter")){
            new TestWindow();
        }

        if(Configuration.parameters.get("action").equals("class")){
            Configuration.parameters.put("language","fr");
            Configuration.parameters.put("titleProcedure","titleProcedure");
            Configuration.parameters.put("organismName","organismName");
            Configuration.parameters.put("liblot","liblot");
            Configuration.parameters.put("urlCryptedFile","http://localhost/");
            Configuration.parameters.put("sessionid","111111111");
            Configuration.parameters.put("filename","test.pdf");
            Configuration.parameters.put("urlCertificat","http://localhost/");
            Configuration.parameters.put("urlDepot","http://localhost/");
            JFrame test = (JFrame) Class.forName("com.oodrive.omnikles.cryptodoc.swing.window."+Configuration.parameters.get("class")).newInstance();
            test.setVisible(true);
        }
        Logs.sp("End Init");
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

    public void initContext() throws IOException{
        URL propertiesUrl = this.getClass().getResource("/cryptodoc.properties");
        if(propertiesUrl != null && propertiesUrl.getFile() != null) {
            contextProperties.load(propertiesUrl.openStream());
            Configuration.version = contextProperties.getProperty("actual.version");
        }
    }

    public void loadLocalConfiguration() throws IOException{
        File localPropertiesFile = new File(System.getProperty("user.home")
                    + File.separatorChar
                    + "cryptodoc.conf");
        if(localPropertiesFile == null || !localPropertiesFile.exists()){
            initLocalConfiguration();
        }else{
            loadLocalProperties(localPropertiesFile);
        }
    }

    public static void saveLocalConfiguration(ProxyConfig proxyConfig) throws IOException{
        File localPropertiesFile = new File(System.getProperty("user.home")
                + File.separatorChar
                + "cryptodoc.conf");
        String content = proxyConfig.toString();
        if(localPropertiesFile.exists() && localPropertiesFile.isFile())
            Files.write(localPropertiesFile.toPath(), content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        else
            Files.write(localPropertiesFile.toPath(), content.getBytes(), StandardOpenOption.CREATE);
        loadLocalProperties(localPropertiesFile);
    }

    public static void initLocalConfiguration() throws IOException{
        File localPropertiesFile = new File(System.getProperty("user.home")
                + File.separatorChar
                + "cryptodoc.conf");
        Configuration.proxy = new ProxyConfig();

        String content = Configuration.proxy.toString();
        Files.write(localPropertiesFile.toPath(), content.getBytes(), StandardOpenOption.CREATE);
    }

    public static void loadLocalProperties(File localPropertiesFile) throws IOException {
        contextProperties.load(new FileInputStream(localPropertiesFile));
        Configuration.version = contextProperties.getProperty("actual.version");
        Configuration.proxy = new ProxyConfig();
        Configuration.proxy.setHost(contextProperties.getProperty("proxy.host"));
        try {
            Configuration.proxy.setPort(Integer.valueOf(contextProperties.getProperty("proxy.port")));
        }catch( NumberFormatException  ex){
            Configuration.proxy.setPort(0);
        }
        Configuration.proxy.setPassword(contextProperties.getProperty("proxy.pass"));
        Configuration.proxy.setUser(contextProperties.getProperty("proxy.user"));
        Configuration.proxy.setAuthenticationType(contextProperties.getProperty("proxy.authentication.type"));
    }

    public void initTextes(String language) throws IOException{
        URL propertiesUrl = this.getClass().getResource("/texts_" + language + ".properties");
        if(propertiesUrl != null && propertiesUrl.getFile() != null) {
            textProperties.load(new InputStreamReader((propertiesUrl.openStream()),"UTF-8"));
        }
    }
}
