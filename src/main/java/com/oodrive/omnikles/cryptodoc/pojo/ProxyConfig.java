package com.oodrive.omnikles.cryptodoc.pojo;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;

public class ProxyConfig {

    private String host;
    private int port;
    private String user;
    private String password;
    private String authenticationType = "no";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        if(authenticationType == null || authenticationType.isEmpty())
            this.authenticationType = "no";
        else
            this.authenticationType = authenticationType;
    }


    @Override
    public String toString(){
        String content = CryptoDoc.textProperties.getProperty("default.proxy.configuration");
        content = content.replace("proxy.host=","proxy.host="+host);
        content = content.replace("proxy.port=","proxy.port="+port);
        content = content.replace("proxy.user=","proxy.user="+user);
        content = content.replace("proxy.pass=","proxy.pass="+password);
        content = content.replace("proxy.authentication.type=no","proxy.authentication.type="+authenticationType);
        return content;
    }
}
