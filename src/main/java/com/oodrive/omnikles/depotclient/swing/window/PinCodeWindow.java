package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.service.AESService;
import com.oodrive.omnikles.depotclient.service.CryptoService;
import com.oodrive.omnikles.depotclient.service.SslConnexionService;
import com.oodrive.omnikles.depotclient.utils.ZipUtils;
import org.bouncycastle.crypto.CryptoException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
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

    public PinCodeWindow(String urlCryptedFile, String sessionid, String filename, MainWindow parent){
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
                File f = ssl.sslDownloadFile(urlCryptedFile, sessionid, filename);
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
                    byte[] content  = ZipUtils.getContentFile(new ZipFile(f), CryptoDocConfiguration.FILENAME_CRYPTED_KEYS);
                    if(kp != null) {
                        secret = aesService.decodeSecretKeyByCertificat(content, kp);
                    }else {
                        System.out.println("aucun certificat selectionné." );
                    }
                } catch (IOException exx) {
                    exx.printStackTrace();
                }
//        if(aesService.secret.getEncoded() != secret)
//            throw new InvalidKeyException("les deux clef ne sont pas indentiques !");

                try {
                    aesService.decryptFileWithSecretKey( new File(CryptoDocConfiguration.activFolder
                                    + File.separatorChar
                                    + CryptoDocConfiguration.FILENAME_CRYPTED_ZIP)
                            , new File(CryptoDocConfiguration.activFolder
                                    + File.separatorChar
                                    + CryptoDocConfiguration.FILENAME_DECRYPTED_ZIP), secret);
                } catch (NoSuchPaddingException |NoSuchAlgorithmException |InvalidAlgorithmParameterException |InvalidKeyException
                        |IOException |BadPaddingException |IllegalBlockSizeException |InvalidParameterSpecException |CryptoException ex) {
                    ex.printStackTrace();
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
