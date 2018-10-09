package com.oodrive.omnikles.cryptodoc.service;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.DepositStatus;
import com.oodrive.omnikles.cryptodoc.pojo.DepositStatusMarches;
import com.oodrive.omnikles.cryptodoc.pojo.ExchangeDocumentState;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.cryptodoc.swing.component.ProgressEntityWrapper;
import com.oodrive.omnikles.cryptodoc.swing.component.ProgressListener;
import com.oodrive.omnikles.cryptodoc.utils.Logs;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
        Logs.sp("getCertificatesWithJSessionId method");
        HttpEntity entity = null;
        try {
            entity = getResponseHttpGet(urlCertificate).getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonCertificate = getStringResponse(entity);
        List<String> certificatsB64 = getJSONCertificates(jsonCertificate);
        return certificatsB64;
    }

    public HashMap<Long, DepositStatus> getDepositStatusesWithJSessionId(String urlDepositStatus) throws JSONException, ConnectionClosedException {
        Logs.sp("getDepositStatusesWithJSessionId method");
        HttpEntity entity = null;
        try {
            entity = getResponseHttpGet(urlDepositStatus).getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonDepositStatus = getStringResponse(entity);
        HashMap<Long, DepositStatus> depositStatuses = null;
        Logs.sp(jsonDepositStatus);
        if(Configuration.isOkMarches){
            depositStatuses = getJSONDepositStatusesMarches(jsonDepositStatus);
        }else{
            depositStatuses = getJSONDepositStatuses(jsonDepositStatus);
        }
        return depositStatuses;
    }

    public File sslDownloadFile(String url, String filename){
        Logs.sp("sslDownloadFile method");
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
        Logs.sp("sslUploadFile method");

        Logs.sp(url);
        CloseableHttpResponse httpResponse= getResponseHttpPostFile(url, file, hashFile);
        StatusLine statusLine = httpResponse.getStatusLine();
        if(statusLine.getStatusCode() != 200)
            throw new UnsupportedOperationException("Error server : "+statusLine.getStatusCode() + "\n" + statusLine.getReasonPhrase() );
        HttpEntity entity = httpResponse.getEntity();
        Header header = httpResponse.getFirstHeader("Content-Disposition");

        String filename = "pod.";
        if(Configuration.isOkMarches){
            filename += "xml";
        }else{
            for(HeaderElement he:header.getElements()){
                if(he.getName().equalsIgnoreCase("attachment"))
                    if(he.getParameter(0) != null && he.getParameter(0).getValue() != null) {
                        filename = he.getParameter(0).getValue();
                        filename = filename.substring(0, filename.lastIndexOf("_"))+".pdf";
                    }
            }
        }
        Logs.sp("sslDownloadFile method");
        Logs.sp("Download File in " + Configuration.activFolder + File.separatorChar + filename);
        File podFile = new File(Configuration.activFolder + File.separatorChar + filename);
        try {
            FileOutputStream out = new FileOutputStream(podFile);
            entity.writeTo(out);
            out.close();
        }catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return podFile;
    }

    public void updateExchangeDocumentState(long documentId, String urlUpdateStatus, long idLot) throws IOException {
        Logs.sp("updateExchangeDocumentState method");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("state", ExchangeDocumentState.OPEN.name()));
        params.add(new BasicNameValuePair("lot_id", ""+idLot));
        getResponseHttpPost(urlUpdateStatus+documentId,params).getEntity();
    }

    public void updateExchangeDocumentState(String urlUpdateStatus, List<NameValuePair> params) throws IOException  {
        Logs.sp("updateExchangeDocumentState method");
        getResponseHttpPost(urlUpdateStatus,params).getEntity();
    }

    /* -------------------------------- PRIVATE ------------------------------*/

    private SSLConnectionSocketFactory initSSL(){
        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            Logs.sp("loadTrustMaterial");
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        Logs.sp("Connection ssl socket ");
        SSLConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        Logs.sp("HttpClient post SSL");
        return sslsf;
    }

    public CloseableHttpClient createHttpClientOrProxy() {
        return createHttpClientOrProxy(true);
    }

    public CloseableHttpClient createHttpClientOrProxy(boolean isNotSSL) {
        HttpClientBuilder hcBuilder = HttpClients.custom();
      //  HttpHost proxySystem = detectSystemProxy();
        HttpHost activProxy;
//        if(proxySystem != null)
//            activProxy = proxySystem;
//        else
        activProxy = setConfigProxy();

        if(activProxy != null){
            hcBuilder.setProxy(activProxy);
            CredentialsProvider credentialsProvider = initializeProxyAuthenticator(
                    new AuthScope(Configuration.proxy.getHost(), Configuration.proxy.getPort()));
            if(credentialsProvider != null)
                hcBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }
        CloseableHttpClient httpClient;
        if(activProxy != null) {
            Logs.sp("getHostName " + activProxy.getHostName());
            Logs.sp("getPort " + activProxy.getPort());
            Logs.sp("getSchemeName " + activProxy.getSchemeName());
            Logs.sp("type " + Configuration.proxy.getAuthenticationType());
            Logs.sp("user " + Configuration.proxy.getUser());
        }
        if(isNotSSL)
            httpClient = hcBuilder.build();
        else
            httpClient = hcBuilder.setSSLSocketFactory(initSSL()).build();
        return httpClient;
    }

    private HttpHost setConfigProxy(){
        if(Configuration.proxy.getHost() != null && !Configuration.proxy.getHost().isEmpty()
                && Configuration.proxy.getPort() != 0){
            return new HttpHost(Configuration.proxy.getHost(), Configuration.proxy.getPort());
        }
        return null;
    }

    /**
     * @return HttpHost if system proxy on or null
     */
    private HttpHost detectSystemProxy() {
        if((System.getProperty("http.proxy.getHost()") != null && !System.getProperty("http.proxy.getHost()").equals("null"))
            || (System.getProperty("https.proxy.getHost()") != null && !System.getProperty("https.proxy.getHost()").equals("null"))){
            int port = 80;
            String protocol = "http";
            if( System.getProperty("https.proxy.getHost()") != null && !System.getProperty("https.proxy.getHost()").equals("null")){
                port = 443;
                protocol = "https";
            }
            if( System.getProperty(protocol + ".proxyPort") != null
                    && !System.getProperty(protocol + ".proxyPort").equals("null") ) {
                port = Integer.parseInt(System.getProperty(protocol + ".proxyPort"));
            }
            if (Configuration.proxy.getUser() == null || Configuration.proxy.getUser().isEmpty()){
                Configuration.proxy.setUser(System.getProperty(protocol + ".proxy.getUser()"));
            }
            if (Configuration.proxy.getPassword() == null || Configuration.proxy.getPassword().isEmpty())
                Configuration.proxy.setPassword(System.getProperty(protocol + ".proxyPassword"));

            String defaultAuthType = "";
            if (System.getProperty("jdk.http.auth.tunneling.disabledSchemes") != null
                    && !System.getProperty("jdk.http.auth.tunneling.disabledSchemes").isEmpty()
                    && !System.getProperty("jdk.http.auth.tunneling.disabledSchemes").equalsIgnoreCase("http")
                    && !System.getProperty("jdk.http.auth.tunneling.disabledSchemes").equalsIgnoreCase("https")){
                defaultAuthType = System.getProperty("jdk.http.auth.tunneling.disabledSchemes");
            }
            Configuration.proxy.setAuthenticationType(defaultAuthType);

            Configuration.proxy.setHost(System.getProperty(protocol + ".proxy.getHost()"));
            Configuration.proxy.setPort(port)  ;
            HttpHost proxy = new HttpHost(Configuration.proxy.getHost(), Configuration.proxy.getPort(), protocol);
            return proxy;
        }
        return null;
    }

    /**
     * @return HttpHost if cryptodoc.conf have proxy, or null
     */
    private CredentialsProvider initializeProxyAuthenticator(AuthScope authScope) {
        if(Configuration.proxy.getAuthenticationType() == null || Configuration.proxy.getAuthenticationType().isEmpty())
            return null;
        if(Configuration.proxy.getUser() == null || Configuration.proxy.getUser().isEmpty())
            Configuration.proxy.setUser(JOptionPane.showInputDialog(new Frame(), "Proxy user name ?"));
        if(Configuration.proxy.getPassword() == null || Configuration.proxy.getPassword().isEmpty())
            Configuration.proxy.setPassword(JOptionPane.showInputDialog(new Frame(), "Proxy password ?"));
        Credentials ntCreds = null;
        switch(Configuration.proxy.getAuthenticationType().toLowerCase()){
            case "ntlm":
                String localMachineName = "";
                String domainName = "";
                try
                {
                    InetAddress addr = java.net.InetAddress.getLocalHost();
                    localMachineName = addr.getHostName();
                    domainName = InetAddress.getLocalHost().getCanonicalHostName();
                    Logs.sp("Hostname of system = " + domainName);
                }
                catch (UnknownHostException ex)
                {
                    Logs.sp("Hostname can not be resolved");
                }
                if (Configuration.proxy.getUser() != null && !Configuration.proxy.getUser().equals("null")
                        && Configuration.proxy.getPassword() != null && !Configuration.proxy.getPassword().equals("null")) {
                    ntCreds = new NTCredentials(Configuration.proxy.getUser(), Configuration.proxy.getPassword(), localMachineName, domainName);
                }
                break;
            case "basic":;
            case "negotiate":;
            case "kerberos":;
            case "digest":
                if (Configuration.proxy.getUser() != null && !Configuration.proxy.getUser().equals("null")
                        && Configuration.proxy.getPassword() != null && !Configuration.proxy.getPassword().equals("null")) {
                    ntCreds = new UsernamePasswordCredentials(Configuration.proxy.getUser(), Configuration.proxy.getPassword());
                }
                break;
        }
        CredentialsProvider credsProvider = new SystemDefaultCredentialsProvider();
        credsProvider.setCredentials(authScope, ntCreds );
        return credsProvider;
    }

    public CloseableHttpResponse getResponseHttpGet(String url) throws IOException {
        if (debug)
            Logs.sp("... Debut connexion ...");
        Logs.sp("HttpClient");
        Logs.sp("url : " + url);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie", "JSESSIONID="+Configuration.parameters.get("sessionid"));
        CloseableHttpClient httpclient = createHttpClientOrProxy(false);
        if(httpclient == null)
            throw new NullPointerException("HTTP client is null !");
        if(httpGet == null)
            throw new NullPointerException("GET Request is null !");
        Logs.sp("Start request");
        return httpclient.execute(httpGet);
    }

    public void sendPostEnveloppeEmpreinte(String url, String hashFile, boolean envoiMailDepotEmprunte) {
        Logs.sp("Envoi de l'empreinte "+hashFile+" a "+url);
        try {
            getResponseHttpPostMultipart(url, getParametersEmpreinteOkMarches(hashFile, envoiMailDepotEmprunte));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private CloseableHttpResponse getResponseHttpPostFile(String url, File file, String hashFile){
        if (debug)
            Logs.sp("... Debut upload file ...");

        MultipartEntityBuilder builderFile = MultipartEntityBuilder.create();
        builderFile.addBinaryBody("file", file,
                ContentType.APPLICATION_OCTET_STREAM,
                file.getName());
        if(Configuration.isOkMarches) {
            ArrayList<NameValuePair> postParameters = getParametersDepotOkMarches();
            for (NameValuePair parameter : postParameters) {
                builderFile.addTextBody(parameter.getName(), parameter.getValue(), ContentType.TEXT_PLAIN);
            }
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

        CloseableHttpClient httpclientSsl = createHttpClientOrProxy(false);

        if(httpclientSsl == null)
            throw new NullPointerException("HTTP client is null !");
        if(httpPost == null)
            throw new NullPointerException("POST Request is null !");
        try {
            Logs.sp("Start request upload ");
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
                Logs.sp(Configuration.parameters.get("ids"));
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
            Logs.sp("Read response");
            StringBuffer responseBuffer = new StringBuffer();
            while ((sLine = in.readLine()) != null) {
                responseBuffer.append(sLine);
            }
            Logs.sp("Reponse : ");
            content = responseBuffer.toString();
            Logs.sp(content);
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

    public HashMap<Long, DepositStatus> getJSONDepositStatusesMarches(String jsonDepositStatus) throws JSONException, ConnectionClosedException {
        JSONObject obj = new JSONObject(jsonDepositStatus);
        JSONArray depositStatusesArray = null;
        try {
            depositStatusesArray = obj.getJSONArray("data");
        }catch(JSONException e){
            throw new ConnectionClosedException("\n" + obj.getJSONObject("status").get("message"));
        }
        HashMap<Long, DepositStatus> depositStatuses = new HashMap<>();
        for(int i=0; i<depositStatusesArray.length(); i++){
            DepositStatusMarches depositStatusMarches = new DepositStatusMarches(depositStatusesArray.getJSONObject(i));
            DepositStatus depositStatus = new DepositStatus(depositStatusMarches);
            depositStatuses.put(depositStatus.getId(), depositStatus);
        }
        return depositStatuses;
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
        CloseableHttpClient httpclientSsl = createHttpClientOrProxy(false);
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
        CloseableHttpClient httpclientSsl = createHttpClientOrProxy(false);
        HttpPost httpPost = new HttpPost(url);
        Header header = new BasicHeader("Cookie", "JSESSIONID=" + Configuration.parameters.get("sessionid"));

        MultipartEntityBuilder builderFile = MultipartEntityBuilder.create();
        for(NameValuePair parameter:parameters) {
            Logs.sp("name : " + parameter.getName() + " - value : " + parameter.getValue());
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
