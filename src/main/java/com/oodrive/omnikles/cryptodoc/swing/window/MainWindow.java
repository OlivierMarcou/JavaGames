package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.CertificatesComboBox;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GenaralPanelTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GeneralTextTemplate;

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

    private GeneralTextTemplate lblSelectCertificate = new GeneralTextTemplate("Selectionner votre certificat : ");


    private CertificatesComboBox listCertificate = new CertificatesComboBox();
    private ButtonTemplate btnSelected = new ButtonTemplate("Selectionner");
    private AESService aes = AESService.getInstance();

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
    public CertificatesComboBox getListCertificate() {
        return listCertificate;
    }

    public MainWindow(){
        setSize(700, 400);

        GenaralPanelTemplate panel = new GenaralPanelTemplate(this);
        JPanel centerPanel = panel.getCenterPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("CryptoDoc");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = Design.PREFERED_SIZE.getWidth();
        c.weighty = Design.PREFERED_SIZE.getHeight();
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        centerPanel.add(lblSelectCertificate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = Design.PREFERED_SIZE.getWidth();
        c.weighty = Design.PREFERED_SIZE.getHeight();
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=2;
        centerPanel.add(listCertificate, c);

        listCertificate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                myBox(evt);
            }
        });
        btnSelected.addActionListener(decryptAction);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        centerPanel.add(btnSelected, c);


    }

    protected void myBox(ActionEvent evt) {
        if (listCertificate.getSelectedItem() != null) {
            System.out.println("DN : " + listCertificate.getSelectedItem().toString());
        }
    }

    public void init(){
        List<KeyPair> certificates = aes.getInstalledCertificates();
        for(KeyPair certificate:certificates){
            listCertificate.addItem(certificate);
            System.out.println(certificate.getPkB64());
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
