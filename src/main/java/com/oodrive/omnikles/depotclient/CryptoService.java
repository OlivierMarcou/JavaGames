package com.oodrive.omnikles.depotclient;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivier on 21/11/16.
 */
public class CryptoService {

    public File crypteByCertificats(File file, List<String> certificats) throws IOException {
        if(file.exists()) {
            try {
                final CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                List<X509Certificate> certificatsX509 = new ArrayList<>();
                for(String certificat:certificats) {
                    InputStream is = new ByteArrayInputStream(certificat.getBytes());
                    certificatsX509.add((X509Certificate) certFactory.generateCertificate(is));
                }
                return AESUtils.encrypt(file, certificatsX509);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            throw new FileNotFoundException("fichier introuvable : "+ file.getAbsolutePath());
        }
        return null;
    }

    public List<KeyPair> getInstalledCertificats(){
        CertificatesManager cm = new CertificatesManager();
        List<KeyPair> certificats =  new ArrayList<>();
        try {
            KeyStore ks = cm.getKeyStore();
            certificats = cm.loadKeyPairsFromKeystore(ks, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certificats;
    }

    public String decryptWindows(File file, KeyPair certificate) throws FileNotFoundException {
        if(file.exists()){
            String decrypte = null;
            try {
                decrypte = AESUtils.decryptByPk(file, certificate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return decrypte;
        }else{
            throw new FileNotFoundException("Fichier introuvable : "+ file.getAbsolutePath());
        }
    }

    public String decryptP12(File file, String p12Filename, char[] password)
            throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        File p12File = new File(p12Filename);
        if(file.exists() ){
            String decrypte = null;
            List<KeyPair> certificats = getKeyPair(password, p12File);
            try {
                decrypte = AESUtils.decryptByPk(file, certificats.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return decrypte;
        }else{
            throw new FileNotFoundException("Fichier introuvable : "+ file.getAbsolutePath());
        }
    }

    public List<KeyPair> getKeyPair(char[] password, File p12File)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        if(p12File.exists()) {
            CertificatesManager cm = new CertificatesManager();
            KeyStore p12 = KeyStore.getInstance("pkcs12");
            p12.load(new FileInputStream(p12File), password);

            try {
                return cm.loadKeyPairsFromKeystore(p12, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
