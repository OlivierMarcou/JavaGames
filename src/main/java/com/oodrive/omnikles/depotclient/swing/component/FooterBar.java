package com.oodrive.omnikles.depotclient.swing.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by olivier on 06/03/17.
 */
public class FooterBar extends JMenuItem{
    private Dimension sizeBar = new   Dimension(600, 26);
    private JFrame parent = null;
    private JLabel footerEastTxt = new JLabel();
    private JLabel footerWestTxt = new JLabel();
    private LineProgressBar lineProgressBar = null;

    public FooterBar(JFrame parent) {
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        footerEastTxt.setForeground(Color.WHITE);
        footerWestTxt.setForeground(Color.WHITE);

        this.parent = parent;
        sizeBar = new Dimension(parent.getWidth() , 26);
        lineProgressBar = new LineProgressBar(parent);
        setPreferredSize(sizeBar);
        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, lineProgressBar);

        add(BorderLayout.EAST, footerEastTxt);
        add(BorderLayout.WEST, footerWestTxt);

parent.pack();
    }

    public void setWestMessage(String message) {
        footerWestTxt.setText("cxcxw "+message);
    }


    public void setEastMessage(String message) {
        footerEastTxt.setText("cxcxw "+message);
    }
}
