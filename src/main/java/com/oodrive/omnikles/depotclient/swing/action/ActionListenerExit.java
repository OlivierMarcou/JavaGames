package com.oodrive.omnikles.depotclient.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by olivier on 24/01/17.
 */
public class ActionListenerExit implements ActionListener {


    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(1);
    }
}
