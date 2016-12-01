package com.oodrive.omnikles.depotclient;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by olivier on 16/09/16.
 */


public class CryptoDoc extends JFrame {

    private static CryptoDoc window = new CryptoDoc();
    private static CryptoService cs = new CryptoService();

    public static void main(String[] args) throws IOException{
        System.out.println("WebStart CryptoDoc !");
        HashMap<String, String> parameters = new HashMap<>();

        String[] keyValue = new String[2];
        for (String parameter: args ){
            int indexEqual = parameter.trim().indexOf("=");
            keyValue[0] = parameter.substring(0, indexEqual);
            keyValue[0].replaceFirst("-","");
            keyValue[1] = parameter.substring(indexEqual+1);
            System.out.println(keyValue[0] + " " + keyValue[1]);
            parameters.put(keyValue[0], keyValue[1]);
        }
        if(parameters.get("action").equals("depot")) {
            depot(parameters);
        }

        if(parameters.get("action").equals("decrypt")){
            openDepot(parameters);
        }

    }

    private static void openDepot(HashMap<String, String> parameters) throws MalformedURLException, FileNotFoundException {
        URL url = new URL(parameters.get("urlCryptedFile"));
        File f = new File(url.getFile().replaceAll( "%20", " " ));
        String resultat = cs.decryptWindows(f);
        System.out.println("Decrypted : "+resultat);
    }

    private static void depot(HashMap<String, String> parameters) throws IOException {
        SslConnexion ssl = new SslConnexion();
        String certificat = ssl.getCertificatWithJSessionId(parameters.get("urlCertificat"), parameters.get("sessionid"));
        if(certificat == null)
            throw new NullPointerException("Aucun certificat trouvé pour : " + parameters.get("urlCertificat"));
        String selectFile = window.fileChooser();
        System.out.println(selectFile);
        File cryptedFile = cs.crypteByCertificat(new File(selectFile), certificat);

        ssl.sslUploadFile(cryptedFile, parameters.get("urlDepot"), parameters.get("sessionid"));
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
