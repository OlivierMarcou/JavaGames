package com.oodrive.omnikles.depotclient.thread;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.Configuration;
import com.oodrive.omnikles.depotclient.service.AESService;
import com.oodrive.omnikles.depotclient.service.SslConnexionService;
import com.oodrive.omnikles.depotclient.service.ZipService;
import com.oodrive.omnikles.depotclient.swing.component.AnimatedProgressBar;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by olivier on 10/02/17.
 */
public class DepositFilesRunnable implements Runnable{

    private ZipService zs = new ZipService();
    private File zip ;
    private List<File> files;
    private AnimatedProgressBar progressBar = null;
    AESService as = new AESService();

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

    public DepositFilesRunnable(){

    }
    @Override
    public void run() {
        zs.setProgressBar(progressBar);
        zs.setJobNumber(0);
        zs.setMaxPercent(25);
        zs.addFilesToNewZip(zip, files);
        System.out.println("zip ok");

        SslConnexionService ssl = new SslConnexionService();
        java.util.List<String> certificats = ssl.getCertificatsWithJSessionId(Configuration.parameters.get("urlCertificat"), Configuration.parameters.get("sessionid"));
        if(certificats == null || certificats.size() <= 0) {
            JOptionPane d = new JOptionPane();
            int retour = d.showConfirmDialog(progressBar.getParent(), CryptoDoc.textProperties.getProperty("depot.page4.sending.result.fail"),
                    CryptoDoc.textProperties.getProperty("depot.page4.sending.result.fail.title"), JOptionPane.ERROR_MESSAGE);
            throw new NullPointerException("Aucun certificat trouvé pour : " + Configuration.parameters.get("urlCertificat"));
        }

        File enveloppe = null;
        //crypte le zip, créé un fichier .crypt et l'ajoute dans enveloppe.zip
        try {
            as.setProgressBar(progressBar);
            as.setJobNumber(1);
            as.setMaxPercent(25);
            enveloppe = as.crypteByCertificats(zip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("crypt ok");
        ssl.setJobNumber(3);
        ssl.setMaxPercent(25);
        ssl.sslUploadFileAndDownloadProof(enveloppe, Configuration.parameters.get("urlDepot"), Configuration.parameters.get("sessionid"), progressBar);
        System.out.println("send ok");
        progressBar.getIcon().setVisible(false);
        progressBar.setText(CryptoDoc.textProperties.getProperty("depot.page4.sending.result.ok"));
    }
}
