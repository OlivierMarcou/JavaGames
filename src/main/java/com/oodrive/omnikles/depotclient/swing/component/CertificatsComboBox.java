package com.oodrive.omnikles.depotclient.swing.component;

import com.oodrive.omnikles.depotclient.pojo.KeyPair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by olivier on 24/01/17.
 */
public class CertificatsComboBox extends JComboBox<KeyPair> implements ActionListener {


    public CertificatsComboBox(){
        addActionListener(this);
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
