package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.service.CryptoService;
import com.oodrive.omnikles.depotclient.service.SslConnexionService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
/**
 * Created by olivier on 04/01/17.
 */
public class PasswordP12Window extends JDialog{

    private CryptoService cs = new CryptoService();

    public PasswordP12Window(String urlCryptedFile, String sessionid, String filename){
        String p12Namefile = fileChooser();

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

        JButton btnAnnul = new JButton("Annuler");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        content.add(btnAnnul, c);

        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                if(!txtPassword.getText().trim().isEmpty()){
                    try {
                        java.util.List<KeyPair> certificats = cs.getKeyPairList(txtPassword.getText().trim().toCharArray(), new File(p12Namefile));
                        KeyPair selectedCertificat = certificats.get(0);
                        try {
                            SslConnexionService ssl = new SslConnexionService();
                            File f = ssl.sslDownloadFile(urlCryptedFile, sessionid, filename);
                            cs.decryptWindows(f, selectedCertificat);
                            System.out.println("Decrypted ! ");
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        setVisible(false);
                    } catch (KeyStoreException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (NoSuchAlgorithmException e1) {
                        e1.printStackTrace();
                    } catch (CertificateException e1) {
                        e1.printStackTrace();
                    }
                    setVisible(false);
//                }
            }
        });
        btnAnnul.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        setAlwaysOnTop(true);
        setVisible(true);
    }

    public String fileChooser() {
        String filename = null;
        String dir = null;
        JFileChooser c = new JFileChooser(System.getenv("HOME"));
        c.setAcceptAllFileFilterUsed(false);
        int rVal = c.showOpenDialog(PasswordP12Window.this);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getSelectedFile().getName();
            dir = c.getCurrentDirectory().toString();
            return dir + File.separatorChar + filename;
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
            filename = null ;
            dir = null;
        }
        return null;
    }
}
