package com.oodrive.omnikles.cryptodoc.swing.component;

import com.oodrive.omnikles.cryptodoc.deposit.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.deposit.service.AESService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by olivier on 24/01/17.
 */
public class CertificatesComboBox extends JComboBox<KeyPair> implements ActionListener {
    AESService aes = AESService.getInstance();

    public CertificatesComboBox(){
        List<KeyPair> certificats = aes.getInstalledCertificates();
        for(KeyPair certificat:certificats){
            addItem(certificat);
            System.out.println(certificat.getPkB64());
        }addActionListener(this);
    }

    protected void initSelected(ActionEvent evt) {
        if (getSelectedItem() != null) {
            System.out.println("DN : " + getSelectedItem().toString());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        initSelected(e);
    }
}
