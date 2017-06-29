package com.oodrive.omnikles.cryptodoc;

import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.service.Decrypt;
import com.oodrive.omnikles.cryptodoc.service.ZipService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.util.zip.ZipFile;

/**
 * Created by olivier on 21/06/17.
 */
public class CryptoTests {

    private ZipService zs = ZipService.getInstance();
    private AESService aes = AESService.getInstance();
    public void decryptOld(File zip, KeyPair pk) {
        Decrypt.openEnveloppe(zip,  zip.getPath().replaceAll(zip.getName(), ""), (RSAPrivateKey) pk.getPrivateKey());
    }

    public void decryptNew(File zip, KeyPair pk){
        byte[] secret = new byte[0];        try {
            Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, Boolean.FALSE);
        } catch (Exception ex) {
        }
        try {
            System.out.println("Zip name :"+zip.getName());
            System.out.println("Zip exist :"+zip.exists());
            System.out.println("Zip path :"+zip.getPath());
            System.out.println("Zip size :"+zip.length());
            System.out.println("FILENAME_CRYPTED_KEYS : " + Configuration.FILENAME_CRYPTED_KEYS);
            byte[] content  = zs.getContentFile(new ZipFile(zip), "ENVELOPPE.key.p7m");
            if(pk != null) {
                System.out.println("Begin decode sercret key ...");
                try {
                    secret = aes.decodeXMLSecretKeyByCertificate(content, new KeyPair(pk.getCertificate(), pk.getPrivateKey(), pk.getAlias()));
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("End decode sercret key ...");
                if(secret == null){
                    error(CryptoDoc.textProperties.getProperty("open.page2.decrypt.secret.fail").replace("<filename>",zip.getName()));
                    return;
                }
            }else {
                System.out.println("aucun certificat selectionné." );
            }
        } catch (IOException exx) {
            exx.printStackTrace();
            error(CryptoDoc.textProperties.getProperty("message.error.text")+ " " + zip.getName());
            return;
        }
        zs.unzip(zip.getPath(), zip.getPath().replaceAll(zip.getName(), ""), false);
        File cryptedFile = new File(Configuration.destinationFolderPath
                + File.separatorChar
                + Configuration.FILENAME_CRYPTED_ZIP);
        try {
            aes.decryptFileWithSecretKeyDESede(cryptedFile
                    , new File(Configuration.destinationFolderPath
                            + File.separatorChar
                            + zip.getName()), secret);
        } catch (Exception e1) {
            e1.printStackTrace();
            error(CryptoDoc.textProperties.getProperty("message.error.text")+ " " + zip.getName());
        }
    }

    private void error(String msg){
        System.err.println(msg);
    }
}
