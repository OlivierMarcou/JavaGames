package com.oodrive.omnikles.cryptodoc.service;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.cryptodoc.swing.component.ProgressEntityWrapper;
import com.oodrive.omnikles.cryptodoc.swing.component.ProgressListener;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
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
public class SslConnexionService{

    private AnimatedProgressBar uploadBar = null;
    private boolean debug = true;
    private int percentMem = -1;
    private int jobNumber = 0;
    private int maxPercent = 100 ;

    public int getMaxPercent() {
        return maxPercent;
    }

    public void setMaxPercent(int maxPercent) {
        this.maxPercent = maxPercent;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public List<String> getCertificatesWithJSessionId(String urlCertificate, String JSessionId){
        System.out.println("getCertificatesWithJSessionId method");
        HttpEntity entity = getResponseHttpGet(urlCertificate, JSessionId).getEntity();

        String jsonCertificate = getStringResponse(entity);
        List<String> certificatsB64 = getJSONCertificates(jsonCertificate);
        if (certificatsB64 != null) return certificatsB64;
        return null;
    }

    public File sslDownloadFile(String url, String JSessionId, String filename){
        System.out.println("sslDownloadFile method");
        File file = new File(Configuration.activFolder + File.separatorChar + filename);
        try {
            HttpEntity entity = getResponseHttpGet(url, JSessionId).getEntity();
            entity.writeTo(new FileOutputStream(file));
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
        getResponseHttpPostFile(url, JSessionId, file).getEntity();
    }

    public File sslUploadFileAndDownloadProof(File file, String url, String JSessionId, AnimatedProgressBar animatedProgressBar){
        this.uploadBar = animatedProgressBar;
        System.out.println("sslUploadFile method");
        HttpEntity entity = getResponseHttpPostFile(url, JSessionId, file).getEntity();

        System.out.println("sslDownloadFile method");
        System.out.println("Download File in " + Configuration.activFolder + File.separatorChar + "pod.pdf");
        File podFile = new File(Configuration.activFolder + File.separatorChar + "pod.pdf");
        try {
            entity.writeTo(new FileOutputStream(podFile));
            return podFile;
        }catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    public void post(String url, List<NameValuePair> parameters) throws  IOException {
        CloseableHttpResponse httpsReponse = null;
        httpsReponse = getResponseHttpPost(url, parameters);
        httpsReponse.close();
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
        System.out.println("url : " + url);
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

        builderFile.addBinaryBody("file", file,
                ContentType.APPLICATION_OCTET_STREAM,
                file.getName());
        HttpEntity multipart = builderFile.build();
        HttpPost httpPost = new HttpPost(url);

        percentMem = -1;
        ProgressListener pListener =
            new ProgressListener() {
                @Override
                public void progress(long reste, long totalBytes, long transfered) {
                    if(uploadBar != null){
                        int percent = Math.round((transfered*maxPercent)/totalBytes);
                        if(percentMem != percent){
                            uploadBar.setActualIcon(percent + (maxPercent*jobNumber));
                            percentMem = percent;
                            uploadBar.setText(CryptoDoc.textProperties.getProperty("depot.page4.sending") +
                                    (percent + (maxPercent*jobNumber)) + "%");
                        }
                    }
                }
            };

        httpPost.setEntity(new ProgressEntityWrapper(multipart, pListener));
        httpPost.setHeader("Cookie", "JSESSIONID="+JSessionId);

        CloseableHttpClient httpclientSsl = initSSL();

        if(httpclientSsl == null)
            throw new NullPointerException("HTTP client is null !");
        if(httpPost == null)
            throw new NullPointerException("POST Request is null !");
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

    private List<String> getJSONCertificates(String jsonCertificate) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonCertificate);
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

    private CloseableHttpResponse getResponseHttpPost(String url, List<NameValuePair> parameters) throws  IOException {
        CloseableHttpClient httpclientSsl = initSSL();
        HttpPost httpPost = new HttpPost(url);
        Header header = new BasicHeader("Cookie", "JSESSIONID=" + Configuration.parameters.get("sessionid"));
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));
        httpPost.setHeader(header);
        if(httpclientSsl == null)
            throw new NullPointerException("HTTPS client is null !");
        if(httpPost == null)
            throw new NullPointerException("POST Request is null !");
        CloseableHttpResponse httpsReponse = httpclientSsl.execute(httpPost);
        return httpsReponse;
    }

    public static SslConnexionService getInstance() {
        if (null == instance) {
            getUniqueInstance__();
        }
        return instance;
    }
    synchronized private static void getUniqueInstance__() {
        instance =  new SslConnexionService();
    }
    private SslConnexionService() {
    }

    private static SslConnexionService instance;
}
