package com.oodrive.omnikles.depotclient;

import javax.swing.*;
import java.awt.*;

/**
 * Created by olivier on 08/12/16.
 */
public class MainWindow extends JFrame {

    public MainWindow(){
        setSize(400, 400);
        setLayout(new GridBagLayout());

        setTitle("CryptoDoc");
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

}
