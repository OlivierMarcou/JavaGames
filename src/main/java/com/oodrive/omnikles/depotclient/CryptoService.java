package com.oodrive.omnikles.depotclient;

import java.io.*;
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

    CertificatesManager cm = new CertificatesManager();

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
        return cm.getInstalledCertificats();
    }

    public KeyPair getKeyPairWithPrivateKey(String alias, String password){
        return cm.getKeyPairWithPrivateKey(alias, password);
    }

    public List<KeyPair> getKeyPairList( char[] password, File p12Filename) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return cm.getKeyPairList(p12Filename, password);
    }

    public void decryptWindows(File file, KeyPair certificate) throws FileNotFoundException {
        if(file.exists()){
            try {
                AESUtils.decryptByPk(file, certificate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            throw new FileNotFoundException("Fichier introuvable : "+ file.getAbsolutePath());
        }
    }

    public void decryptP12(File file, String p12Filename, char[] password)
            throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        File p12File = new File(p12Filename);

        if(file.exists() ){
            List<KeyPair> certificats = cm.getKeyPairList(p12File, password);
            try {
                AESUtils.decryptByPk(file, certificats.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            throw new FileNotFoundException("Fichier introuvable : "+ file.getAbsolutePath());
        }
    }

}
