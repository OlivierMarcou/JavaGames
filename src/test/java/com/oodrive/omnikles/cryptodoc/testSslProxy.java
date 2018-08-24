package com.oodrive.omnikles.cryptodoc;

import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;
import com.oodrive.omnikles.cryptodoc.utils.Logs;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class testSslProxy {

    private SslConnexionService ssl = SslConnexionService.getInstance();

    @Before
    public void init(){
        try {
            (new CryptoDoc(new String[]{"test=", "test1="})).loadLocalConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testproxyAuthOk(){
        Logs.sp(System.getProperty("http.proxyHost"));
        try {
            Logs.sp(ssl.getResponseHttpGet("http://www.google.fr").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testSslproxyAuthOk(){
        Logs.sp(System.getProperty("https.proxyHost"));
        try {
            Logs.sp(ssl.getResponseHttpGet("https://www.google.fr").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSystemproxyAuthOk(){

//        System.setProperty("http.proxySet", "true");
//        System.setProperty("http.proxyHost", "192.168.5.74");
//        System.setProperty("http.proxyPort", "808");
//        System.setProperty("http.proxyUser", "oliv");
//        System.setProperty("http.proxyPassword", "1234");
//        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "ntlm");

        Logs.sp(System.getProperty("https.proxyHost"));
        try {
            Logs.sp(ssl.getResponseHttpGet("https://www.google.fr").hashCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
