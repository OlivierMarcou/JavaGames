package com.oodrive.omnikles.depotclient;

import com.oodrive.omnikles.depotclient.pojo.KeyPair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by olivier on 08/12/16.
 */
public class MainWindow extends JFrame {

    private JLabel lblSelectCertificat = new JLabel("Selectionner votre certificat : ");
    private JComboBox<KeyPair> listCertificats = new JComboBox<>();
    private JButton btnSelected = new JButton("Selectionner");
    private CryptoService cs = new CryptoService();
    private HashMap<String, String> parameters = new HashMap<>();

    private ActionListener decryptAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Start Decrypt !");
            JDialog message = new CodePinWindow(parameters.get("urlCryptedFile"),
                    parameters.get("sessionid"),
                    parameters.get("filename"), (KeyPair)listCertificats.getSelectedItem());
        }
    };

    private ActionListener decryptActionP12 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Start Decrypt !");
            JDialog message = new PasswordP12Window(parameters.get("urlCryptedFile"), parameters.get("sessionid"), parameters.get("filename"));
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
        content.add(lblSelectCertificat, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=2;
        content.add(listCertificats, c);

        JButton btnP12 = new JButton("Or Upload P12 File !");
        btnP12.addActionListener(decryptActionP12);

        listCertificats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                myBox(evt);
            }
        });
        btnSelected.addActionListener(decryptAction);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        content.add(btnSelected, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=2;
        c.gridwidth=1;
        content.add(btnP12, c);

    }

    protected void myBox(ActionEvent evt) {
        if (listCertificats.getSelectedItem() != null) {
            System.out.println("DN : " + listCertificats.getSelectedItem().toString());
        }
    }

    public void init(HashMap<String, String> parameters){
        this.parameters = parameters;
        List<KeyPair> certificats = cs.getInstalledCertificats();
        for(KeyPair certificat:certificats){
            listCertificats.addItem(certificat);
            System.out.println(certificat.getPkB64());
        }
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
