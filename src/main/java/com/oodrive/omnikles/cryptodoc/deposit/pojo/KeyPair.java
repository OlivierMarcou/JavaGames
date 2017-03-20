package com.oodrive.omnikles.cryptodoc.deposit.pojo;

import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
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
        initX509CertificateB64();
        initPkB64();
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
        String dn = String.valueOf(certificate.getSubjectDN());
        LdapName ln = null;
        try {
            ln = new LdapName(dn);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }

        for(Rdn rdn : ln.getRdns()) {
            if(rdn.getType().equalsIgnoreCase("CN")) {
                return rdn.toString();
            }
        }
        return null;
    }

    private void initX509CertificateB64(){
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            X509CertificateB64 = X509Factory.BEGIN_CERT + "\n" + encoder.encodeBuffer(certificate.getEncoded()) + X509Factory.END_CERT;
        } catch (CertificateEncodingException e) {
        }
    }


    private void initPkB64(){
        if( privateKey != null && privateKey.getEncoded() != null){
            BASE64Encoder encoder = new BASE64Encoder();
            pkB64 = X509Factory.BEGIN_CERT + "\n" + encoder.encodeBuffer(privateKey.getEncoded()) + X509Factory.END_CERT;
        }
    }

    public String getX509CertificateB64(){
        return X509CertificateB64;
    }

    public String getPkB64(){
        return pkB64;
    }
    public int getSizeB64(){
        return getX509CertificateB64().length();
    }
}
