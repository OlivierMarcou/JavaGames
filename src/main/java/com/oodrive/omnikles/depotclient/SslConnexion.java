package com.oodrive.omnikles.depotclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivier on 21/11/16.
 */
public class SslConnexion {

    private boolean debug = true;

    public String getCertificatWithJSessionId(String urlCertificat, String JSessionId){
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
                JSONArray certificats = obj.getJSONArray("certificats");
                List<String> certificatsB64 = new ArrayList();
                for(int i=0; i<certificats.length(); i++){
                    certificatsB64.add(certificats.get(i).toString());
                }
                return certificatsB64.get(0); //TODO: ensuite on retournera la liste qui servira à crypter plusieur fois la clé AES256
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sslUploadFile(File file, String url, String JSessionId){
        System.out.println("sslUploadFile method");
        if (debug)
            System.out.println("... Debut upload crypted file ...");

        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            System.out.println("loadTrustMaterial");
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());//Works with all ssl ?
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        HttpPost httpPost = new HttpPost(url);
        System.out.println("Connection ssl socket ");
        SSLConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        MultipartEntityBuilder builderFile = MultipartEntityBuilder.create();

        // This attaches the file to the POST:
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        builderFile.addBinaryBody("file", fis,
                ContentType.APPLICATION_OCTET_STREAM,
                file.getName());

        HttpEntity multipart = builderFile.build();

        httpPost.setEntity(multipart);
        System.out.println("HttpClient post SSL");
        CloseableHttpClient httpclientSsl = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        httpPost.setHeader("Cookie", "JSESSIONID="+JSessionId);
        if(httpclientSsl == null)
            throw new NullPointerException("HTTP client is null !");
        if(httpPost == null)
            throw new NullPointerException("GET Request is null !");

        CloseableHttpResponse response = null;
        try {
            System.out.println("Start request upload TenderLink depot");
            response = httpclientSsl.execute(httpPost);
            HttpEntity entity = response.getEntity();
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            String sLine = "";
            System.out.println("Read response TenderLink");
            StringBuffer responseBuffer = new StringBuffer();
            while ((sLine = in.readLine()) != null) {
                responseBuffer.append(sLine);
            }
            System.out.println("Reponse TenderLink : ");
            System.out.println(responseBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
