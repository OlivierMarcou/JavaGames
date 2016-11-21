package com.oodrive.omnikles.depotclient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivier on 21/11/16.
 */
public class SslConnexion {
    
    public static final Logger logger = LoggerFactory.getLogger(SslConnexion.class);

    private String urlSigning = "https://olivier.net/tender-link-rest/tenderlink/user/_signin";
    private String urlCertificat = "https://olivier.net/tender-link-rest/tenderlink/certificat/634";
    private boolean debug = false;
    private String username = "admin";
    private String password = "1234";
    private String userType = "ADMIN";
    private String JSessionId = "";
    private CloseableHttpClient httpclient;
    public String certificat = null;

    public int connexion() throws IOException {
        if (debug)
            logger.info("... Debut connexion ...");

        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

        HttpPost httpPost = new HttpPost(urlSigning);
        List<NameValuePair> params = new ArrayList<>(3);
        params.add(new BasicNameValuePair("j_password", password));
        params.add(new BasicNameValuePair("j_username", username));
        params.add(new BasicNameValuePair("j_user_type", userType));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            logger.info("Code retour connexion : " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            getCertificat();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
        return 0;
    }

    public void getCertificat(){
        HttpGet httpGet = new HttpGet(urlCertificat);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            String sLine = "";
            StringBuffer responseBuffer = new StringBuffer();
            while ((sLine = in.readLine()) != null) {
                responseBuffer.append(sLine);
            }
            String jsonCertificat = responseBuffer.toString();

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
