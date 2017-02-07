package com.oodrive.omnikles.depotclient.service;

import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.utils.CertificatesUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidParameterSpecException;
import java.util.List;

/**
 * Created by olivier on 21/11/16.
 */
public class CryptoService {

    AESService as = new AESService();

    public File crypteByCertificats(File file, List<String> certificats) throws IOException {
        if(file.exists()) {


            List<KeyPair> certs = CertificatesUtils.getInstalledCertificats();
            System.out.println("utils");

            File zipFile = null;
            try {
                zipFile = as.zipKeyFile(certs, CryptoDocConfiguration.activFolder
                        + File.separatorChar
                        + CryptoDocConfiguration.FILENAME_ZIP);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                as.encryptFileWithSecretKey(file, zipFile);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            return zipFile;
        }else{
            throw new FileNotFoundException("fichier introuvable : "+ file.getAbsolutePath());
        }
    }

    public List<KeyPair> getInstalledCertificats(){
        return CertificatesUtils.getInstalledCertificats();
    }

    public KeyPair getKeyPairWithPrivateKey(String alias, String password) throws Exception {
        return CertificatesUtils.getKeyPairWithAlias(alias, password);
    }

    public List<KeyPair> getKeyPairList( char[] password, File p12Filename) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return CertificatesUtils.getKeyPairList(p12Filename, password);
    }

}
