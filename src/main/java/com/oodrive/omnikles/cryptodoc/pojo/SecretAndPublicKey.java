package com.oodrive.omnikles.cryptodoc.pojo;

import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.utils.Base64;
import com.oodrive.omnikles.cryptodoc.utils.Logs;

import java.util.List;

public class SecretAndPublicKey {

    private byte[] secretKey;
    private String publicCertificate;

    private AESService aes = AESService.getInstance();

    public byte[] getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    public String getPublicCertificate() {
        return publicCertificate;
    }

    public void setPublicCertificate(String publicCertificate) {
        this.publicCertificate = publicCertificate;
    }

    public SecretAndPublicKey(String p7mFile){
        int indexCert = 0;
        String certAanalyser = "";
        byte[] bytedecrypted = null;
        while (indexCert != -1) {
            indexCert = p7mFile.indexOf("<ds:X509Certificate>");
            if (indexCert > 0) {
                indexCert = indexCert + 20;
                certAanalyser = p7mFile.substring(indexCert, p7mFile.indexOf("</ds:X509Certificate>"));
                certAanalyser = certAanalyser.replaceAll("\n", "");
                certAanalyser = certAanalyser.replaceAll("-----BEGIN CERTIFICATE-----", "");
                certAanalyser = certAanalyser.replaceAll("-----END CERTIFICATE-----", "");
                publicCertificate = certAanalyser;
                List<KeyPair> kps = aes.getInstalledCertificates();
                for(KeyPair kp:kps){
                    String certInstalled = kp.getX509CertificateB64().replaceAll("\n", "");
                    certInstalled = certInstalled.replaceAll("-----BEGIN CERTIFICATE-----", "");
                    certInstalled = certInstalled.replaceAll("-----END CERTIFICATE-----", "");
                    if(certInstalled.equals(certAanalyser)){
                        Logs.sp("loop - recherche de certificat - Fin de la boucle : la cle privee a ete trouvee ");
                        int index2 = p7mFile.indexOf("<ds:EncryptedKey>") + 17;
                        String encryptedKey = p7mFile.substring(index2, p7mFile.indexOf("</ds:EncryptedKey>"));
                        try {
                            encryptedKey = encryptedKey.replaceAll("\n", "");
                            Logs.sp("Valeur de la cle symetrique cryptee = " + encryptedKey + "\n\n");
                            byte[] bytecrypted = Base64.decode(encryptedKey.getBytes());
                            Logs.sp(" ------------------- Crypted byte ? => " + bytecrypted.length);

                            AESService aes = AESService.getInstance();
                            bytedecrypted = aes.decryptSecretKey(kp, bytecrypted);
                            Logs.sp("cle symetrique decryptee - taille=" + bytedecrypted.length);
                            if ((bytedecrypted != null) || bytedecrypted.length > 0) {
                                secretKey = bytedecrypted;
                                return;
                            }
                        } catch (Exception exc) {
                            Logs.sp(2,"Exception lors du decryptage=" + exc);
                        }
                    }
                }
                p7mFile = p7mFile.substring(p7mFile.indexOf("</ds:EncryptedExchangeKey>") + 26);
            } else {
                Logs.sp("Erreur : certificat pour dechiffrer non trouve dans l'enveloppe.");
            }
        }
    }
}
