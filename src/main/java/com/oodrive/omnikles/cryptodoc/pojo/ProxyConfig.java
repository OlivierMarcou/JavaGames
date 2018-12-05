package com.oodrive.omnikles.cryptodoc.pojo;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;

public class ProxyConfig {

    private String host;
    private Integer port;
    private String user;
    private String password;
    private String authenticationType;


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
}
