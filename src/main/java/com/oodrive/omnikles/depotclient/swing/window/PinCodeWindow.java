package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.service.AESService;
import com.oodrive.omnikles.depotclient.service.CryptoService;
import com.oodrive.omnikles.depotclient.service.SslConnexionService;
import com.oodrive.omnikles.depotclient.utils.ZipUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * Created by olivier on 04/01/17.
 */
public class PinCodeWindow extends JDialog{

    private JTextField txtPassword = new JTextField();
    private MainWindow parent;
    private CryptoService cs = new CryptoService();

    @Override
    public MainWindow getParent() {
        return parent;
    }

    public void setParent(MainWindow parent) {
        this.parent = parent;
    }

    public JTextField getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(JTextField txtPassword) {
        this.txtPassword = txtPassword;
    }

    public PinCodeWindow(MainWindow parent){
        setSize(300, 200);
        Container content = getContentPane();
                content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        content.add(new JLabel ( "Password : "), c);

        txtPassword.setColumns(25);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=0;
        c.gridwidth=1;
        content.add(txtPassword, c);

        JButton btnDecrypt = new JButton("Valider");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        content.add(btnDecrypt, c);

        JButton annul = new JButton("Annuler");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        content.add(annul, c);
        btnDecrypt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SslConnexionService ssl = new SslConnexionService();
                //TODO : deja téléchargé, mais donner quand meme la possibilité de le refaire dans le web start
                // changer ici pas un filechooser de sequestre
                File f = ssl.sslDownloadFile(
                        CryptoDocConfiguration.parameters.get("urlCryptedFile"),
                        CryptoDocConfiguration.parameters.get("sessionid"),
                        CryptoDocConfiguration.parameters.get("filename"));
                //Initialise la clé privé avec le code pin
                KeyPair kp = null;
                try {
                    kp = cs.getKeyPairWithPrivateKey(
                            ((KeyPair)parent.getListCertificat().getSelectedItem()).getAlias(),
                            txtPassword.getText());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                AESService aesService = new AESService();

                byte[] secret = new byte[0];
                try {
                    System.out.println("Zip name :"+f.getName());
                    System.out.println("Zip exist :"+f.exists());
                    System.out.println("Zip path :"+f.getPath());
                    System.out.println("Zip size :"+f.length());
                    System.out.println("FILENAME_CRYPTED_KEYS : " + CryptoDocConfiguration.FILENAME_CRYPTED_KEYS);
                    byte[] content  = ZipUtils.getContentFile(new ZipFile(f), CryptoDocConfiguration.FILENAME_CRYPTED_KEYS);
                    if(kp != null) {
                        System.out.println("Begin decode sercret key ...");
                        secret = aesService.decodeSecretKeyByCertificat(content, kp);
                        System.out.println("End decode sercret key ...");
                    }else {
                        System.out.println("aucun certificat selectionné." );
                    }
                } catch (IOException exx) {
                    exx.printStackTrace();
                }
                ZipUtils.unzip(CryptoDocConfiguration.FILENAME_ZIP, CryptoDocConfiguration.activFolder);
                File cryptedFile = new File(CryptoDocConfiguration.activFolder
                                    + File.separatorChar
                                    + CryptoDocConfiguration.FILENAME_CRYPTED_ZIP);
                try {
                    aesService.decryptFileWithSecretKey(cryptedFile
                            , new File(CryptoDocConfiguration.activFolder
                                    + File.separatorChar
                                    + CryptoDocConfiguration.FILENAME_DECRYPTED_ZIP), secret);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        annul.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        setAlwaysOnTop(true);
    }
}
