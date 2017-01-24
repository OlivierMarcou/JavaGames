package com.oodrive.omnikles.depotclient.swing.component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by olivier on 24/01/17.
 */
public class PinCodeTextField extends JTextField implements ActionListener{

    public static String pinCode = null;

    public PinCodeTextField(){
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pinCode = getText().trim();
    }
}
