package com.oodrive.omnikles.depotclient;

import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.services.AESService;
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
public class CodePinWindow extends JDialog{

    private CryptoService cs = new CryptoService();

    CodePinWindow(String urlCryptedFile, String sessionid, String filename, KeyPair selectedCertificat){

        setSize(300, 200);
        Container content = getContentPane();
                content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        content.add(new JLabel ( "Password : "), c);

        JTextArea txtPassword = new JTextArea ();
                txtPassword.setColumns(25);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=0;
        c.gridwidth=1;
        content.add(txtPassword, c);

        JButton go = new JButton("Valider");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        content.add(go, c);

        JButton annul = new JButton("Annuler");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        content.add(annul, c);

        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                if(!txtPassword.getText().trim().isEmpty()){
                SslConnexion ssl = new SslConnexion();
                File f = ssl.sslDownloadFile(urlCryptedFile, sessionid, filename);
                //Initialise la clé privé avec le code pin
                KeyPair kp = null;
                try {
                    kp = cs.getKeyPairWithPrivateKey(selectedCertificat.getAlias(), txtPassword.getText().trim());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                 selectedCertificat.setPrivateKey(kp.getPrivateKey());
                KeyPair keyPair = selectedCertificat;
                AESService aesService = new AESService();

                byte[] secret = new byte[0];
                try {
                    byte[] content  = ZipUtils.getContentFile(new ZipFile(f),"ENVELOPPE.key.p7m");
                    if(keyPair != null) {
                        secret = aesService.decodeSecretKeyByCertificat(content, keyPair);
                    }else {
                        System.out.println("aucun certificat selectionné." );
                    }
                } catch (IOException exx) {
                    exx.printStackTrace();
                }
//        if(aesService.secret.getEncoded() != secret)
//            throw new InvalidKeyException("les deux clef ne sont pas indentiques !");

                try {
                    aesService.decryptFileWithSecretKey( new File("ENVELOPPE.crypt"), new File("ENVELOPPE.decrypt"), secret);
                } catch (NoSuchPaddingException |NoSuchAlgorithmException |InvalidAlgorithmParameterException|InvalidKeyException
                        |IOException |BadPaddingException |IllegalBlockSizeException |InvalidParameterSpecException |CryptoException ex) {
                    ex.printStackTrace();
                }
            }
        });
        annul.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        setAlwaysOnTop(true);
        setVisible(true);
    }

}
