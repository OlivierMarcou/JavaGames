package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.pojo.Configuration;
import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.service.AESService;
import com.oodrive.omnikles.depotclient.swing.component.CertificatsComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Created by olivier on 08/12/16.
 */
public class MainWindow extends JFrame {

    private JLabel lblSelectCertificat = new JLabel("Selectionner votre certificat : ");


    private CertificatsComboBox listCertificat = new CertificatsComboBox();
    private JButton btnSelected = new JButton("Selectionner");
    private AESService aes = new AESService();

    private PinCodeWindow pinCodeWindow = new PinCodeWindow( this);


    private ActionListener decryptAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Start Decrypt !");
            pinCodeWindow.setVisible(true);
        }
    };


    public PinCodeWindow getPinCodeWindow() {
        return pinCodeWindow;
    }
    public CertificatsComboBox getListCertificat() {
        return listCertificat;
    }

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
        content.add(listCertificat, c);


        listCertificat.addActionListener(new ActionListener() {
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


    }

    protected void myBox(ActionEvent evt) {
        if (listCertificat.getSelectedItem() != null) {
            System.out.println("DN : " + listCertificat.getSelectedItem().toString());
        }
    }

    public void init(){
        List<KeyPair> certificats = aes.getInstalledCertificats();
        for(KeyPair certificat:certificats){
            listCertificat.addItem(certificat);
            System.out.println(certificat.getPkB64());
        }
    }

    public String fileChooser() {
        String filename = null;
        String dir = null;
        JFileChooser c = new JFileChooser(Configuration.activFolder);
        c.setAcceptAllFileFilterUsed(false);
        int rVal = c.showOpenDialog(MainWindow.this);
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
