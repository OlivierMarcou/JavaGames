package com.oodrive.omnikles.depotclient;

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
import java.util.HashMap;
import java.util.List;

/**
 * Created by olivier on 08/12/16.
 */
public class MainWindow extends JFrame {

    private JLabel jSelectCertificat = new JLabel("Selectionner votre certificat : ");
    private JComboBox<KeyPair> jCertificats = new JComboBox<>();
    private JButton jSelected = new JButton("Selectionner");
    private CryptoService cs = new CryptoService();
    private HashMap<String, String> parameters = new HashMap<>();

    private ActionListener decryptAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Start Decrypt !");

            SslConnexion ssl = new SslConnexion();
            File f = ssl.sslDownloadFile(parameters.get("urlCryptedFile"), parameters.get("sessionid"), parameters.get("filename"));
            String resultat = null;
            KeyPair selectedCertificat = (KeyPair)jCertificats.getSelectedItem();
            try {
                resultat = cs.decryptWindows(f, selectedCertificat);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            System.out.println("Decrypted : " + resultat);
        }
    };

    private ActionListener decryptActionP12 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Start Decrypt !");


            String p12Namefile = fileChooser();
            JDialog message = new JDialog();

            message.setSize(300, 200);
            Container content = message.getContentPane();
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
                    if(!txtPassword.getText().trim().isEmpty()){
                        try {
                            List<KeyPair> certificats = cs.getKeyPair(txtPassword.getText().trim().toCharArray(), new File(p12Namefile));
                            KeyPair selectedCertificat = certificats.get(0);
                            String resultat = null;
                            try {
                                SslConnexion ssl = new SslConnexion();
                                File f = ssl.sslDownloadFile(parameters.get("urlCryptedFile"), parameters.get("sessionid"), parameters.get("filename"));
                                resultat = cs.decryptWindows(f, selectedCertificat);
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            message.setVisible(false);
                            System.out.println("Decrypted : " + resultat);
                        } catch (KeyStoreException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (NoSuchAlgorithmException e1) {
                            e1.printStackTrace();
                        } catch (CertificateException e1) {
                            e1.printStackTrace();
                        }
                        message.setVisible(false);
                    }
                }
            });
            annul.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    message.setVisible(false);
                }
            });
            message.setAlwaysOnTop(true);
            message.setVisible(true);
        }
    };

    public MainWindow(){
        setSize(700, 400);

        Container content = getContentPane();
        content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("CryptoDoc");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        content.add(jSelectCertificat, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=2;
        content.add(jCertificats, c);

        JButton btnP12 = new JButton("Or Upload P12 File !");
        btnP12.addActionListener(decryptActionP12);

        jCertificats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                myBox(evt);
            }
        });
        jSelected.addActionListener(decryptAction);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        content.add(jSelected, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=2;
        c.gridwidth=1;
        content.add(btnP12, c);
    }

    protected void myBox(ActionEvent evt) {
        if (jCertificats.getSelectedItem() != null) {
            System.out.println(jCertificats.getSelectedItem().toString());
        }
    }

    public void init(HashMap<String, String> parameters){
        this.parameters = parameters;
        List<KeyPair> certificats = cs.getInstalledCertificats();
        for(KeyPair certificat:certificats)
            jCertificats.addItem(certificat);
    }


    public String fileChooser() {
        String filename = null;
        String dir = null;
        JFileChooser c = new JFileChooser(System.getenv("HOME"));
        c.setAcceptAllFileFilterUsed(false);
        int rVal = c.showOpenDialog(MainWindow.this);
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

    public void depot() throws IOException {
        SslConnexion ssl = new SslConnexion();
        List<String> certificats = ssl.getCertificatsWithJSessionId(parameters.get("urlCertificat"), parameters.get("sessionid"));
        if(certificats == null || certificats.size() <= 0)
            throw new NullPointerException("Aucun certificat trouvé pour : " + parameters.get("urlCertificat"));
        String selectFile = fileChooser();
        System.out.println(selectFile);
        File cryptedFile = cs.crypteByCertificats(new File(selectFile), certificats);
        ssl.sslUploadFile(cryptedFile, parameters.get("urlDepot"), parameters.get("sessionid"));
    }

}
