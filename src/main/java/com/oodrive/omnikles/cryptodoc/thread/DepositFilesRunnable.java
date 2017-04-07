package com.oodrive.omnikles.cryptodoc.thread;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;
import com.oodrive.omnikles.cryptodoc.service.ZipService;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;
import org.json.JSONException;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Created by olivier on 10/02/17.
 */
public class DepositFilesRunnable implements Runnable{

    private ZipService zs = ZipService.getInstance();
    private File zip ;
    private List<File> files;
    private AnimatedProgressBar progressBar = null;
    AESService as = AESService.getInstance();

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
        System.out.println("zip ok");

        SslConnexionService ssl = SslConnexionService.getInstance();
        List<String> certificats = null;
        try {
            certificats = ssl.getCertificatesWithJSessionId(Configuration.parameters.get("urlCertificat"));
        } catch (JSONException e) {
            JOptionPane.showMessageDialog(progressBar, CryptoDoc.textProperties.getProperty("message.error.text"),
            CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        if(certificats == null || certificats.size() <= 0) {
            JOptionPane.showMessageDialog(progressBar.getParent(), CryptoDoc.textProperties.getProperty("message.error.text"),
                    CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
            throw new NullPointerException("Aucun certificat trouvé pour : " + Configuration.parameters.get("urlCertificat"));
        }

        File enveloppe = null;
        //crypte le zip, créé un fichier .crypt et l'ajoute dans enveloppe.zip
        try {
            as.setProgressBar(progressBar);
            as.setJobNumber(1);
            as.setMaxPercent(25);
            enveloppe = as.cryptedByCertificates(zip, certificats);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(progressBar.getParent(), CryptoDoc.textProperties.getProperty("message.error.text"),
                    CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("crypt ok");
        ssl.setJobNumber(3);
        ssl.setMaxPercent(25);
        File podFile = ssl.sslUploadFileAndDownloadProof(enveloppe, Configuration.parameters.get("urlDepot"), progressBar);
        System.out.println("send ok");
        progressBar.finish(podFile);
    }

}
