package com.oodrive.omnikles.cryptodoc.swing.component;

import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.swing.component.template.easter.MenuEasterEggs;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by olivier on 06/03/17.
 */
public class FooterBar extends JMenuItem{
    private Dimension sizeBar = new   Dimension(Design.WIDTH_FOOTER, Design.HEIGHT_FOOTER);
    private JFrame parent = null;
    private JLabel footerEastTxt = new JLabel();
    private JLabel footerWestTxt = new JLabel();
    private LineProgressBar lineProgressBar = null;

    private int count = 0;
    public FooterBar(JFrame parent) {
        addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            count++;
            if(count == 5) {
                new MenuEasterEggs();
                count = 0;
            }
        }
    });
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        footerEastTxt.setForeground(Color.WHITE);
        footerWestTxt.setForeground(Color.WHITE);
        Border border = footerEastTxt.getBorder();
        Border margin = new EmptyBorder(10,10,10,10);
        footerEastTxt.setBorder(new CompoundBorder(border, margin));
        footerWestTxt.setBorder(new CompoundBorder(border, margin));
//        footerEastTxt.setBorder(new EmptyBorder(0, Design.CONTENT_MARGIN, 0, 0));
//        footerWestTxt.setBorder(new EmptyBorder(0, 0, 0, Design.CONTENT_MARGIN));

        this.parent = parent;
        sizeBar = new Dimension(parent.getWidth() , Design.HEIGHT_FOOTER);
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
        footerWestTxt.setText(page +"/"+ lineProgressBar.getEtapes());
    }

    public void setPagesNumber(int number){
        lineProgressBar.setEtapes(number);
    }
}
