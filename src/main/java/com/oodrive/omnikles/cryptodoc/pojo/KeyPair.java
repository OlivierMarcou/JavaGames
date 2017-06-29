package com.oodrive.omnikles.cryptodoc.pojo;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Enumeration;

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

    public KeyPair(String certificate, String privateKey, String alias) throws CertificateException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificate.getBytes()));
        this.certificate = cert;
        setPrivateKey(privateKey);
        this.alias = alias;
        initX509CertificateB64();
        initPkB64();
    }

    public KeyPair(String p12Path, String password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore p12 = KeyStore.getInstance("pkcs12");
        p12.load(new FileInputStream(p12Path), password.toCharArray());
        Enumeration e = p12.aliases();
        while (e.hasMoreElements()) {
            String alias = (String) e.nextElement();
            this.certificate  = (X509Certificate) p12.getCertificate(alias);
            this.privateKey  = (PrivateKey) p12.getKey(alias, password.toCharArray());
            this.alias = alias;
            initX509CertificateB64();
            initPkB64();
        }
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


    public void setPrivateKey(String privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
        // Read in the key into a String
        StringBuilder pkcs8Lines = new StringBuilder();
        BufferedReader rdr = new BufferedReader(new StringReader(privateKey));
        String line;
        while ((line = rdr.readLine()) != null) {
            pkcs8Lines.append(line);
        }

        // Remove the "BEGIN" and "END" lines, as well as any whitespace

        String pkcs8Pem = pkcs8Lines.toString();
        pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "");
        pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "");
        pkcs8Pem = pkcs8Pem.replaceAll("\\s+","");

        // Base64 decode the result
        BASE64Decoder decode = new BASE64Decoder();
        byte [] pkcs8EncodedBytes = decode.decodeBuffer(pkcs8Pem);

        // extract the private key

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        this.privateKey = privKey;
    }
}
