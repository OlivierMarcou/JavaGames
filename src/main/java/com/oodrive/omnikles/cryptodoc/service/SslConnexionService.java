package com.oodrive.omnikles.cryptodoc.service;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.DepositStatus;
import com.oodrive.omnikles.cryptodoc.pojo.ExchangeDocumentState;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.cryptodoc.swing.component.ProgressEntityWrapper;
import com.oodrive.omnikles.cryptodoc.swing.component.ProgressListener;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
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

    public List<String> getCertificatesB64WithJSessionId(String urlCertificate ) throws JSONException {
        System.out.println("getCertificatesWithJSessionId method");
        HttpEntity entity = getResponseHttpGet(urlCertificate).getEntity();

        String jsonCertificate = getStringResponse(entity);
        List<String> certificatsB64 = getJSONCertificates(jsonCertificate);
        return certificatsB64;
    }

    public HashMap<Long, DepositStatus> getDepositStatusesWithJSessionId(String urlDepositStatus) throws JSONException, ConnectionClosedException {
        System.out.println("getDepositStatusesWithJSessionId method");
        HttpEntity entity = getResponseHttpGet(urlDepositStatus).getEntity();

        String jsonDepositStatus = getStringResponse(entity);
        HashMap<Long, DepositStatus> depositStatuses = getJSONDepositStatuses(jsonDepositStatus);
        return depositStatuses;
    }

    public File sslDownloadFile(String url, String filename){
        System.out.println("sslDownloadFile method");
        File file = new File(Configuration.activFolder + File.separatorChar + filename);
        try {
            HttpEntity entity = getResponseHttpGet(url).getEntity();
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

    public File sslUploadFileAndDownloadProof(File file, String url,  AnimatedProgressBar animatedProgressBar, String hashFile){
        this.uploadBar = animatedProgressBar;
        System.out.println("sslUploadFile method");

        CloseableHttpResponse httpResponse= getResponseHttpPostFile(url, file, hashFile);
        StatusLine statusLine = httpResponse.getStatusLine();
        if(statusLine.getStatusCode() != 200)
            throw new UnsupportedOperationException("Error server : "+statusLine.getStatusCode() + "\n" + statusLine.getReasonPhrase() );
        HttpEntity entity = httpResponse.getEntity();

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

    public void updateExchangeDocumentState(long documentId, String urlUpdateStatus) throws IOException {
        System.out.println("updateExchangeDocumentState method");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("state", ExchangeDocumentState.OPEN.name()));
        HttpEntity entity = getResponseHttpPost(urlUpdateStatus+documentId,params).getEntity();
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

    private CloseableHttpResponse getResponseHttpGet(String url){
        if (debug)
            System.out.println("... Debut connexion ...");
        System.out.println("HttpClient");
        System.out.println("url : " + url);
        CloseableHttpClient httpclientSsl = initSSL();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie", "JSESSIONID="+Configuration.parameters.get("sessionid"));
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

    public void sendPostEnveloppeEmpreinte(String url, String hashFile, boolean envoiMailDepotEmprunte) {
        System.out.println("Envoi de l'empreinte "+hashFile+" a "+url);
        try {
            getResponseHttpPostMultipart(url, getParametersEmpreinteOkMarches(hashFile, envoiMailDepotEmprunte));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private CloseableHttpResponse getResponseHttpPostFile(String url, File file, String hashFile){
        if (debug)
            System.out.println("... Debut upload file ...");

        MultipartEntityBuilder builderFile = MultipartEntityBuilder.create();
        builderFile.addBinaryBody("file", file,
                ContentType.APPLICATION_OCTET_STREAM,
                file.getName());

        ArrayList<NameValuePair> postParameters = getParametersDepotOkMarches();
        for(NameValuePair parameter:postParameters) {
            builderFile.addTextBody(parameter.getName(), parameter.getValue(), ContentType.TEXT_PLAIN);
        }
        builderFile.addTextBody("hash_code", hashFile);

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
        httpPost.setHeader("Cookie", "JSESSIONID="+Configuration.parameters.get("sessionid"));

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

    private ArrayList<NameValuePair> getParametersEmpreinteOkMarches(String hashFile, boolean envoiMailDepotEmprunte) {
        ArrayList<NameValuePair> postParameters = null;
        if(Configuration.isOkMarches) {
            try {
                String[] idsStr = Configuration.parameters.get("ids").split("_");// <= idDossier +"_" + idLot + "_" + idUser
                postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("rfq_id", idsStr[0]));
                postParameters.add(new BasicNameValuePair("idlot", idsStr[1]));
                postParameters.add(new BasicNameValuePair("user", idsStr[2]));
                if(!envoiMailDepotEmprunte)
                    postParameters.add(new BasicNameValuePair("typeEnvoi", "envoiuntemps"));
                postParameters.add(new BasicNameValuePair("empreinte", hashFile));
//              postParameters.add(new BasicNameValuePair("myEnvp", this.getParameter("dematfr.envptype"));
            }catch(NullPointerException ex){
                ex.printStackTrace();
            }catch(IndexOutOfBoundsException ex){
                ex.printStackTrace();
            }
        }
        return postParameters;
    }

    private ArrayList<NameValuePair> getParametersDepotOkMarches() {
        ArrayList<NameValuePair> postParameters = null;
        if(Configuration.isOkMarches) {
            try {
                String[] idsStr = Configuration.parameters.get("ids").split("_");// <= idDossier +"_" + idLot + "_" + idUser
                postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("rfq_id", idsStr[0]));
                postParameters.add(new BasicNameValuePair("idlot", idsStr[1]));
                postParameters.add(new BasicNameValuePair("user", idsStr[2]));
//              postParameters.add(new BasicNameValuePair("organisme", idOrganisme));
                //pour l'envoie du fichier en plusieurs partie partnum -> partie actuelle,  maxpart -> nombre total des parties
//                postParameters.add(new BasicNameValuePair("partnum", partnum));
//                postParameters.add(new BasicNameValuePair("maxpart", maxpart));
            }catch(NullPointerException ex){
                ex.printStackTrace();
            }catch(IndexOutOfBoundsException ex){
                ex.printStackTrace();
            }
        }
        return postParameters;
    }

    public static String getHashFile(File file) {
        String hash = null;
        try {
            FileInputStream fin = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] buf = new byte[4096];
            int bread;
            while ((bread = fin.read(buf)) != -1){
                md.update(buf, 0, bread);
            }
            byte[] bytes = md.digest();
            fin.close();

            Hex hex = new Hex();
            hash = new String(hex.encode(bytes));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hash;
    }

    public static String getHashFileB64(File file) {
        try {
            FileInputStream fin = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] buf = new byte[4096];
            int bread;
            while ((bread = fin.read(buf)) != -1){
                md.update(buf, 0, bread);
            }
            byte[] bytes = md.digest();
            fin.close();
            BASE64Encoder encoder = new BASE64Encoder();
            return  encoder.encode(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
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

    private List<String> getJSONCertificates(String jsonCertificate) throws JSONException {
        JSONObject obj  = new JSONObject(jsonCertificate);
        String certificateAttributName = "certificatesB64";

        JSONArray certificats = obj.getJSONArray(certificateAttributName);
        List<String> certificatsB64 = new ArrayList();
        for(int i=0; i<certificats.length(); i++){
            certificatsB64.add(certificats.get(i).toString().replaceAll("\r", ""));
        }
        return certificatsB64;
    }

    private HashMap<Long, DepositStatus> getJSONDepositStatuses(String jsonDepositStatus) throws JSONException, ConnectionClosedException {
        JSONObject obj = new JSONObject(jsonDepositStatus);
        JSONArray depositStatusesArray = null;
        try {
            depositStatusesArray = obj.getJSONArray("depositsStatus");
        }catch(JSONException e){
            throw new ConnectionClosedException("\n" + obj.getJSONObject("status").get("message"));
        }
        HashMap<Long, DepositStatus> depositStatuses = new HashMap<>();
        for(int i=0; i<depositStatusesArray.length(); i++){
            String line = depositStatusesArray.get(i).toString();
            line = line.replaceAll("[{}]", "");
            DepositStatus depositStatus = new DepositStatus(line.split(","));
            depositStatuses.put(depositStatus.getId(), depositStatus);
        }
        return depositStatuses;
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

    private CloseableHttpResponse getResponseHttpPostMultipart(String url, List<NameValuePair> parameters) throws  IOException {
        CloseableHttpClient httpclientSsl = initSSL();
        HttpPost httpPost = new HttpPost(url);
        Header header = new BasicHeader("Cookie", "JSESSIONID=" + Configuration.parameters.get("sessionid"));

        MultipartEntityBuilder builderFile = MultipartEntityBuilder.create();
        for(NameValuePair parameter:parameters) {
            System.out.println("name : " + parameter.getName() + " - value : " + parameter.getValue());
            builderFile.addTextBody(parameter.getName(), parameter.getValue(), ContentType.TEXT_PLAIN);
        }
        HttpEntity multipart = builderFile.build();
        httpPost.setEntity(new HttpEntityWrapper(multipart));
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
