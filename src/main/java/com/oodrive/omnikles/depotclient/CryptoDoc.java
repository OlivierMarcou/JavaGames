package com.oodrive.omnikles.depotclient;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by olivier on 16/09/16.
 */


public class CryptoDoc extends JFrame {

    public static final Logger logger = Logger.getLogger(CryptoDoc.class);

    public static void main(String[] args) throws IOException {
        CryptoDoc window = new CryptoDoc();
        CryptoService cs = new CryptoService();
        String resultat = cs.crypteByCertificat(new File(window.fileChooser()));
        logger.debug("Crypté : "+resultat);
        resultat = cs.decryptWindows(resultat);
        logger.debug("Decrypté : "+resultat);
    }

    public CryptoDoc(){
        setSize(200, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public String fileChooser() {
        String filename = null;
        String dir = null;
        JFileChooser c = new JFileChooser(System.getenv("HOME"));
//        FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                "*.*", "*");
//        c.addChoosableFileFilter(filter);
        c.setAcceptAllFileFilterUsed(false);
//        c.setFileFilter(filter);
        int rVal = c.showOpenDialog(CryptoDoc.this);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getSelectedFile().getName();
            dir = c.getCurrentDirectory().toString();
            return dir+"/"+filename;
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
            filename = null ;
            dir = null;
        }
        return null;
    }

    public void load(String path){

    }
}
