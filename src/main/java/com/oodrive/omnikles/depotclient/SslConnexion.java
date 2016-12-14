package com.oodrive.omnikles.depotclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
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

    public List<String> getCertificatsWithJSessionId(String urlCertificat, String JSessionId){
        System.out.println("getCertificatWithJSessionId method");
        HttpEntity entity = getResponseHttpGet(urlCertificat, JSessionId).getEntity();

        String jsonCertificat = getStringResponse(entity);
        List<String> certificatsB64 = getJSONCertificats(jsonCertificat);
        if (certificatsB64 != null) return certificatsB64;
        return null;
    }

    public File sslDownloadFile(String url, String JSessionId, String filename){
        System.out.println("getCertificatWithJSessionId method");
        File file = new File(System.getProperty("user.home") + File.separatorChar + filename);
        try {
            HttpEntity entity = getResponseHttpGet(url, JSessionId).getEntity();
            BufferedInputStream bis = new BufferedInputStream(entity.getContent());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            int inByte;
            while ((inByte = bis.read()) != -1) bos.write(inByte);
            bis.close();
            bos.close();
        }catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
        return file;
    }

    public void sslUploadFile(File file, String url, String JSessionId){
        System.out.println("sslUploadFile method");
        HttpEntity entity = getResponseHttpPostFile(url, JSessionId, file).getEntity();
        getStringResponse(entity);
    }


    /* -------------------------------- PRIVATE ------------------------------*/



    private CloseableHttpClient initSSL(){
        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            System.out.println("loadTrustMaterial");
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        System.out.println("Connection ssl socket ");
        SSLConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        System.out.println("HttpClient post SSL");
        return HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
    }

    private CloseableHttpResponse getResponseHttpGet(String url, String JSessionId){
        if (debug)
            System.out.println("... Debut connexion ...");
        System.out.println("HttpClient");
        CloseableHttpClient httpclientSsl = initSSL();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie", "JSESSIONID="+JSessionId);
        if(httpclientSsl == null)
            throw new NullPointerException("HTTP client is null !");
        if(httpGet == null)
            throw new NullPointerException("GET Request is null !");
        System.out.println("Start request");
        try {
            return httpclientSsl.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CloseableHttpResponse getResponseHttpPostFile(String url, String JSessionId, File file){
        if (debug)
            System.out.println("... Debut upload file ...");

        MultipartEntityBuilder builderFile = MultipartEntityBuilder.create();
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
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(multipart);
        httpPost.setHeader("Cookie", "JSESSIONID="+JSessionId);

        CloseableHttpClient httpclientSsl = initSSL();

        if(httpclientSsl == null)
            throw new NullPointerException("HTTP client is null !");
        if(httpPost == null)
            throw new NullPointerException("POST Request is null !");

        CloseableHttpResponse response = null;
        try {
            System.out.println("Start request upload ");
            return httpclientSsl.execute(httpPost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getStringResponse(HttpEntity entity) {
        String content = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            String sLine = "";
            System.out.println("Read response");
            StringBuffer responseBuffer = new StringBuffer();
            while ((sLine = in.readLine()) != null) {
                responseBuffer.append(sLine);
            }
            System.out.println("Reponse : ");
            content = responseBuffer.toString();
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private List<String> getJSONCertificats(String jsonCertificat) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonCertificat);
            JSONArray certificats = obj.getJSONArray("certificats");
            List<String> certificatsB64 = new ArrayList();
            for(int i=0; i<certificats.length(); i++){
                certificatsB64.add(certificats.get(i).toString().replaceAll("\r", ""));
            }
            return certificatsB64;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
