package com.oodrive.omnikles.depotclient;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivier on 21/11/16.
 */
public class CryptoService {

    public static final Logger logger = Logger.getLogger(CryptoService.class);

    public String crypteByCertificat(File fichier) throws IOException {
        SslConnexion ssl = new SslConnexion();
        ssl.connexion();
        String certificat = ssl.certificat;
        String resultat = null;
        try {
            final CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            InputStream is = new ByteArrayInputStream(certificat.getBytes());
            X509Certificate certificatX509 = (X509Certificate) certFactory.generateCertificate(is);
            resultat = AESUtils.encrypt(fichier, certificatX509);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultat;
    }

    public String decryptWindows(String decrypte) {
        CertificatesManager cm = new CertificatesManager();
        List<KeyPair> certificats =  new ArrayList<>();
        try {
            KeyStore ks = cm.getKeyStore();
            certificats = cm.loadKeyPairsFromKeystore(ks);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            decrypte = AESUtils.decryptByPk("pom.xml" + ".pk7", certificats.get(0).getPrivateKey());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return decrypte;
    }
}
