package com.oodrive.omnikles.depotclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by olivier on 21/11/16.
 */
public class SslConnexion {
    
    private String urlCertificat = null;
    private boolean debug = true;
    private String JSessionId = null;
    public String certificat = null;

    public String getUrlCertificat() {
        return urlCertificat;
    }

    public void setUrlCertificat(String urlCertificat) {
        this.urlCertificat = urlCertificat;
    }

    public String getJSessionId() {
        return JSessionId;
    }

    public void setJSessionId(String JSessionId) {
        this.JSessionId = JSessionId;
    }

    public void setCertificat(String certificat) {
        this.certificat = certificat;
    }

    public void getCertificatWithJSessionId(){
        System.out.println("getCertificatWithJSessionId method");
        if (debug)
            System.out.println("... Debut connexion ...");

        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            System.out.println("loadTrustMaterial");
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        HttpGet httpGet = new HttpGet(urlCertificat);
        System.out.println("Connection ssl socket ");
        SSLConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        System.out.println("HttpClient");
        CloseableHttpClient httpclientSsl = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        httpGet.setHeader("Cookie", "JSESSIONID="+JSessionId);
        if(httpclientSsl == null)
            throw new NullPointerException("HTTP client is null !");
        if(httpGet == null)
            throw new NullPointerException("GET Request is null !");
        CloseableHttpResponse response = null;
        try {
            System.out.println("Start request TenderLink");
            response = httpclientSsl.execute(httpGet);
            HttpEntity entity = response.getEntity();
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            String sLine = "";
            System.out.println("Read response TenderLink");
            StringBuffer responseBuffer = new StringBuffer();
            while ((sLine = in.readLine()) != null) {
                responseBuffer.append(sLine);
            }
            System.out.println("Reponse TenderLink : ");
            String jsonCertificat = responseBuffer.toString();
            System.out.println(jsonCertificat);
            JSONObject obj = null;
            try {
                obj = new JSONObject(jsonCertificat);
                certificat = obj.getJSONObject("certificat").getString("certificat");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
