package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.swing.action.ActionListenerExit;

import javax.swing.*;
import java.awt.*;

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
        close.addActionListener(new ActionListenerExit());
        add(close);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
    }
}
