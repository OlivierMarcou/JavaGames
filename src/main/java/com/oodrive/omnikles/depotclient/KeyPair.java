package com.oodrive.omnikles.depotclient;

import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;

import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class KeyPair {

    private X509Certificate certificate;


    private String alias;
    private PrivateKey privateKey;
    private String X509CertificateB64 = null;
    private String pkB64 = null;

    public KeyPair(X509Certificate certificate, PrivateKey privateKey, String alias) {
        this.certificate = certificate;
        this.privateKey = privateKey;
        this.alias = alias;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString(){
        return String.valueOf(certificate.getIssuerDN() );
    }

    public String getX509CertificateB64(){
        if(X509CertificateB64 == null){
            BASE64Encoder encoder = new BASE64Encoder();
            try {
                X509CertificateB64 = X509Factory.BEGIN_CERT + "\n" + encoder.encodeBuffer(certificate.getEncoded()) + X509Factory.END_CERT;
            } catch (CertificateEncodingException e) {
            }
            }
        return X509CertificateB64;
    }

    public String getPkB64(){
        if(pkB64 == null){
            BASE64Encoder encoder = new BASE64Encoder();
            pkB64 = X509Factory.BEGIN_CERT + "\n" + encoder.encodeBuffer(privateKey.getEncoded()) + X509Factory.END_CERT;
        }
        return pkB64;
    }

    public int getSizeB64(){
        return getX509CertificateB64().length();
    }
}
