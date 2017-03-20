package com.oodrive.omnikles.cryptodoc.deposit.swing.window;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by olivier on 08/12/16.
 */
public class CloseWindow extends JFrame{


    public CloseWindow(){
        setSize(400, 100);
        setLayout(new GridBagLayout());
        setAlwaysOnTop(true);
        setTitle("CryptoDoc");
        setUndecorated(true);
        JLabel texte =new JLabel("<html> Votre dossier est crypté et <br> a été déposé sur le serveur.<br> Vous pouvez fermer la fenetre de dépot.</html>");
        add(texte);
        JButton close =new JButton("FERMER");
        close.setForeground(Color.red);
        close.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        add(close);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
    }
}
