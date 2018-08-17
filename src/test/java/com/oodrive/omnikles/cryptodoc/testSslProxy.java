package com.oodrive.omnikles.cryptodoc;

import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;
import com.oodrive.omnikles.cryptodoc.utils.Logs;
import org.junit.Before;
import org.junit.Test;

public class testSslProxy {

    private SslConnexionService ssl = SslConnexionService.getInstance();

    @Before
    public void init(){

//        System.setProperty("http.proxySet", "true");
//        System.setProperty("http.proxyHost", "192.168.5.72");
//        System.setProperty("http.proxyPort", "808");
//        System.setProperty("http.proxyUser", "");
//        System.setProperty("http.proxyPassword", "");

    }

    @Test
    public void testproxyAuthOk(){
        Logs.sp(System.getProperty("http.proxyHost"));
        Logs.sp(ssl.getResponseHttpGet("https://www.google.fr").toString());
    }

    @Test
    public void testSslproxyAuthOk(){
        Logs.sp(System.getProperty("https.proxyHost"));
        Logs.sp(ssl.getResponseHttpGet("https://www.google.fr").toString());
    }

}
