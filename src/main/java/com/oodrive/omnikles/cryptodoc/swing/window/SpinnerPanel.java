package com.oodrive.omnikles.cryptodoc.swing.window;

import javax.swing.*;
import java.awt.*;

public class SpinnerPanel extends JPanel{

    private ImageIcon spinnerIcon =  new ImageIcon(new ImageIcon(OpenReceivership.class.getResource("/load.gif")).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));

    public SpinnerPanel(){
        setOpaque(false);
        setFocusable(false);
        setLayout(new BorderLayout());
        JLabel loadingIcon = new JLabel();
        loadingIcon.setOpaque(false);
        loadingIcon.setIcon(spinnerIcon);
        add(loadingIcon,BorderLayout.CENTER);
        setVisible(false);
    }
}
