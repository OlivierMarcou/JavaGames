package com.oodrive.omnikles.depotclient.swing.component;

import com.oodrive.omnikles.depotclient.pojo.Configuration;

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
        footerEastTxt.setText( Configuration.version);
        add(BorderLayout.EAST, footerEastTxt);
        add(BorderLayout.WEST, footerWestTxt);

        lineProgressBar.setEtapeActuelle(5);
    }

    public void setWestMessage(String message) {
        footerWestTxt.setText(" "+message);
    }


    public void setEastMessage(String message) {
        footerEastTxt.setText(" "+message);
    }

    public void setActualPage(int page){
        lineProgressBar.setEtapeActuelle(page);
        footerWestTxt.setText(page +" / "+ lineProgressBar.getEtapes());
    }

    public void setPagesNumber(int number){
        lineProgressBar.setEtapes(number);
    }
}
