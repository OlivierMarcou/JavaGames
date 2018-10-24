package com.oodrive.omnikles.cryptodoc.thread;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.service.CryptOkMarchesService;
import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;
import com.oodrive.omnikles.cryptodoc.service.ZipService;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.cryptodoc.utils.Logs;

import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by olivier on 10/02/17.
 */
public class DepositFilesRunnable extends Thread{

    private ZipService zs = ZipService.getInstance();
    private File zip ;
    private List<File> files;
    private AnimatedProgressBar progressBar = null;
    AESService as = AESService.getInstance();
    private CryptOkMarchesService cryptOkMarchesService = CryptOkMarchesService.getInstance();

    public AnimatedProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(AnimatedProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public ZipService getZs() {
        return zs;
    }

    public void setZs(ZipService zs) {
        this.zs = zs;
    }

    public File getZip() {
        return zip;
    }

    public void setZip(File zip) {
        this.zip = zip;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public void run() {
        zs.setProgressBar(progressBar);
        zs.setJobNumber(0);
        zs.setMaxPercent(25);
        zs.addFilesToNewZip(zip, files);
        Logs.sp("zip ok");

        SslConnexionService ssl = SslConnexionService.getInstance();
        List<String> certificatesB64 = null;
        try {
            certificatesB64 = ssl.getCertificatesB64WithJSessionId(Configuration.parameters.get("urlCertificat"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(progressBar,
                    CryptoDoc.textProperties.getProperty("message.error.json.text"),
                    CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if(certificatesB64 == null || certificatesB64.size() <= 0) {
            JOptionPane.showMessageDialog(progressBar.getParent(),
                    CryptoDoc.textProperties.getProperty("message.error.nocertificate.text"),
                    CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
            throw new NullPointerException("Aucun certificat trouvé pour : " + Configuration.parameters.get("urlCertificat"));
        }

        File enveloppe = null;
        //crypte le zip, créé un fichier .crypt et l'ajoute dans enveloppe.zip
        try {
            as.setProgressBar(progressBar);
            as.setJobNumber(1);
            as.setMaxPercent(25);
            if(Configuration.isOkMarches){
                List<KeyPair> keyPairs = new ArrayList<>();
                for(String certificateB64: certificatesB64)
                    keyPairs.add(new KeyPair(certificateB64));
                String pathCryptedKeyFile = Configuration.activFolder + File.separator + Configuration.FILENAME_FOLDERZIP + File.separator + Configuration.FILENAME_CRYPTED_KEYS;
                byte[] symKey = cryptOkMarchesService.genereSymKeyFile(pathCryptedKeyFile, keyPairs);
                enveloppe = cryptOkMarchesService.cryptFileWithSymKey(symKey, zip, pathCryptedKeyFile);
            }else{
                enveloppe = as.cryptedByCertificates(zip, certificatesB64);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(progressBar.getParent(), CryptoDoc.textProperties.getProperty("message.error.text"),
                    CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
        }
        Logs.sp("crypt ok");
        ssl.setJobNumber(3);
        ssl.setMaxPercent(25);
        File podFile = null;
        try {
            String hashFile = null;
            if(Configuration.isOkMarches){
                hashFile = ssl.getHashFileB64(enveloppe);
                ssl.sendPostEnveloppeEmpreinte(Configuration.parameters.get("urlEmpreinte"), hashFile, false);
            }else{
                hashFile = ssl.getHashFile(enveloppe);
            }
            podFile = ssl.sslUploadFileAndDownloadProof(enveloppe, Configuration.parameters.get("urlDepot"), progressBar, hashFile);
        }catch(UnsupportedOperationException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(progressBar.getParent(), CryptoDoc.textProperties.getProperty("message.error.text"),
                    CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(Configuration.isOkMarches) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_YYYY_hh_mm_ss");
            File oldFile = new File(Configuration.activFolder + File.separator + Configuration.FILENAME_FOLDERZIP);
            oldFile.renameTo(new File(Configuration.activFolder + File.separator + Configuration.FILENAME_FOLDERZIP
                    + sdf.format(new Date())));
        }

        Logs.sp("send ok");
        progressBar.finish(podFile);
        this.interrupt();
    }

}
