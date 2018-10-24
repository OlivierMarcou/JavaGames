package com.oodrive.omnikles.cryptodoc.pojo;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;

import java.net.*;
import java.util.List;

public class ProxyConfig {

    private String host;
    private Integer port;
    private String user;
    private String password;
    private String authenticationType;

    private Proxy defaultProxy;

    public Proxy getDefaultProxy() {
        return defaultProxy;
    }

    public void setDefaultProxy(Proxy defaultProxy) {

        InetSocketAddress addr = (InetSocketAddress) defaultProxy.address();
        authenticationType = defaultProxy.type().name();
        host = addr.getHostName();
        port = addr.getPort();

        this.defaultProxy = defaultProxy;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public ProxyConfig(){
        try {
            findProxy(new URI("www.google.fr"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        String content = CryptoDoc.textProperties.getProperty("default.proxy.configuration");
        if(host != null)
            content = content.replace("proxy.host=","proxy.host="+host);
        if(port != null)
            content = content.replace("proxy.port=","proxy.port="+port);
        if(user != null)
            content = content.replace("proxy.user=","proxy.user="+user);
        if(password != null)
            content = content.replace("proxy.pass=","proxy.pass="+password);
        if(authenticationType != null)
            content = content.replace("proxy.authentication.type=","proxy.authentication.type="+authenticationType);
        return content;
    }

    private void findProxy(URI uri)
    {
        try
        {
            ProxySelector selector = ProxySelector.getDefault();
            List<Proxy> proxyList = selector.select(uri);
            if (proxyList.size() > 1) {
                defaultProxy = proxyList.get(0);
                return;
            }
        }
        catch (IllegalArgumentException e)
        {
        }
        detectSystemProxy();
    }

    private void detectSystemProxy() {
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
            if (this.user == null || this.user.isEmpty())
                this.user = System.getProperty(protocol + ".proxy.getUser()");

            if (this.password == null || this.password.isEmpty())
                this.password = System.getProperty(protocol + ".proxyPassword");

            String defaultAuthType = "";
            if (System.getProperty("jdk.http.auth.tunneling.disabledSchemes") != null
                    && !System.getProperty("jdk.http.auth.tunneling.disabledSchemes").isEmpty()
                    && !System.getProperty("jdk.http.auth.tunneling.disabledSchemes").equalsIgnoreCase("http")
                    && !System.getProperty("jdk.http.auth.tunneling.disabledSchemes").equalsIgnoreCase("https")){
                defaultAuthType = System.getProperty("jdk.http.auth.tunneling.disabledSchemes");
            }
            this.authenticationType = defaultAuthType;
            this.host = System.getProperty(protocol + ".proxy.getHost()");
            this.port = port;

        }
    }


}
