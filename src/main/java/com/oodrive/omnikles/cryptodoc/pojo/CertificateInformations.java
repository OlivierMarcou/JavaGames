package com.oodrive.omnikles.cryptodoc.pojo;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by olivier on 03/04/17.
 */
public class CertificateInformations {

    private X509Certificate x509Certificate;

    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }

    public void setX509Certificate(X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    public String getB64Certificate() {
        return b64Certificate;
    }

    public void setB64Certificate(String b64Certificate) {
        this.b64Certificate = b64Certificate;
    }

    private String b64Certificate;

    public CertificateInformations(String b64Certificate){
        this.b64Certificate = b64Certificate;
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        byte[] bytes=b64Certificate.getBytes();
        try {
            this.x509Certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(bytes));
        } catch (CertificateException e) {
            e.printStackTrace();
        }

    }

}
