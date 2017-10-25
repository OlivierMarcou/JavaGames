package com.oodrive.omnikles.cryptodoc;

import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.service.ZipService;
import com.oodrive.omnikles.cryptodoc.utils.Logs;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * Created by olivier on 21/06/17.
 */
public class CryptoTests {

    private ZipService zs = ZipService.getInstance();
    private AESService aes = AESService.getInstance();

    public void decryptNew(File zip, KeyPair pk){
        byte[] secret = new byte[0];
        try {
            Logs.sp("Zip name :"+zip.getName());
            Logs.sp("Zip exist :"+zip.exists());
            Logs.sp("Zip path :"+zip.getPath());
            Logs.sp("Zip size :"+zip.length());
            Logs.sp("FILENAME_CRYPTED_KEYS : " + Configuration.FILENAME_CRYPTED_KEYS);
            byte[] content  = zs.getContentFile(new ZipFile(zip), Configuration.FILENAME_CRYPTED_KEYS);
            if(pk != null) {
                Logs.sp("Begin decode sercret key ...");
//                try {
//                    secret = aes.decryptSecretKey(content, new KeyPair(pk.getCertificate(), pk.getPrivateKey(), pk.getAlias()));
//                } catch (CertificateException e) {
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                Logs.sp("End decode sercret key ...");
                if(secret == null){
                    error(CryptoDoc.textProperties.getProperty("open.page2.decrypt.secret.fail").replace("<filename>",zip.getName()));
                    return;
                }
            }else {
                Logs.sp("aucun certificat selectionné." );
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
//        try {
//            aes.decryptFileWithSecretKeyDESede(cryptedFile
//                    , new File(Configuration.destinationFolderPath
//                            + File.separatorChar
//                            + zip.getName()), secret);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//            error(CryptoDoc.textProperties.getProperty("message.error.text")+ " " + zip.getName());
//        }
    }

    private void error(String msg){
        System.err.println(msg);
    }
}
