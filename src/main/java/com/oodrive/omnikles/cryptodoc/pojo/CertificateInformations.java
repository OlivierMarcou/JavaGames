package com.oodrive.omnikles.cryptodoc.pojo;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by olivier on 03/04/17.
 */
public class CertificateInformations {

    private static String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    private static String END_CERTIFICATE = "-----END CERTIFICATE-----";
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
        if(b64Certificate.indexOf(BEGIN_CERTIFICATE) == -1)
            b64Certificate =  BEGIN_CERTIFICATE +"\n"+b64Certificate;
        if(b64Certificate.indexOf(END_CERTIFICATE) == -1)
            b64Certificate = b64Certificate +"\n"+END_CERTIFICATE ;

        byte[] bytes=b64Certificate.getBytes();
        try {
            this.x509Certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(bytes));
        } catch (CertificateException e) {
            e.printStackTrace();
        }

    }

	private String insertIntoString(String string, String insert, int splitLength) {
		if(splitLength <= 0) {
			return string;
		}
		String result = new String();
		for (int i = 0; i < string.length() - 1; i += splitLength) {
			if(i + splitLength <= string.length() - 1) {
				result += string.substring(i, i + splitLength) + insert;
			} else {
				result += string.substring(i);
			}
		}
		return result;
	}

}
