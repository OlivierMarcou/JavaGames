package com.oodrive.omnikles.cryptodoc.swing.component;

import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.utils.Logs;

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
            Logs.sp(certificat.getPkB64());
        }addActionListener(this);
    }

    protected void initSelected(ActionEvent evt) {
        if (getSelectedItem() != null) {
            Logs.sp("DN : " + getSelectedItem().toString());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        initSelected(e);
    }
}
