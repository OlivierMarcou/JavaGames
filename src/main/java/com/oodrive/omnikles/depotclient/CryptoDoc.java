package com.oodrive.omnikles.depotclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;


/**
 * Created by olivier on 16/09/16.
 */


public class CryptoDoc {

    private static CloseWindow closeWindow = new CloseWindow();
    private static MainWindow mainWindow = new MainWindow();

    private static CryptoService cs = new CryptoService();

    public static void main(String[] args) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        System.out.println("WebStart CryptoDoc !");
        System.out.println(System.getProperty("user.home"));
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
        if(parameters.get("action").equals("debug")){
            File f =new File("plop.txt.crypt");
            cs.decryptTest(f, "newkey.p12", "omnikles".toCharArray());
        }

    }

    private static void openDepot(HashMap<String, String> parameters) throws MalformedURLException, FileNotFoundException {
        SslConnexion ssl = new SslConnexion();
        File f = ssl.sslDownloadFile(parameters.get("urlCryptedFile"), parameters.get("sessionid"), parameters.get("filename"));
        String resultat = cs.decryptWindows(f);
        System.out.println("Decrypted : "+resultat);
    }

    private static void depot(HashMap<String, String> parameters) throws IOException {
        SslConnexion ssl = new SslConnexion();
        List<String> certificats = ssl.getCertificatsWithJSessionId(parameters.get("urlCertificat"), parameters.get("sessionid"));
        if(certificats == null || certificats.size() <= 0)
            throw new NullPointerException("Aucun certificat trouvé pour : " + parameters.get("urlCertificat"));
        String selectFile = mainWindow.fileChooser();
        System.out.println(selectFile);
        File cryptedFile = cs.crypteByCertificats(new File(selectFile), certificats);

        ssl.sslUploadFile(cryptedFile, parameters.get("urlDepot"), parameters.get("sessionid"));
        mainWindow.setVisible(true);
    }

}
