package com.oodrive.omnikles.cryptodoc.deposit.swing.component;

import com.oodrive.omnikles.cryptodoc.deposit.pojo.Design;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by olivier on 07/02/17.
 */
public class InteractiveLabel extends JLabel implements MouseListener {

    private JPanel parent;
    public boolean isSelected = false;

    InteractiveLabel(String title, JPanel parent){
        super(title);
        this.parent = parent;
        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if( getForeground() != Design.BG_COLOR2) {
            isSelected = true;
            this.setForeground(Design.BG_COLOR2);
        }else{
            this.setForeground(Design.FG_COLOR);
            isSelected = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
