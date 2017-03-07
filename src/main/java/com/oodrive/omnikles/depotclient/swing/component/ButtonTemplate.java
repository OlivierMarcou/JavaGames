package com.oodrive.omnikles.depotclient.swing.component;

import com.oodrive.omnikles.depotclient.pojo.Design;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by olivier on 07/03/17.
 */
public class ButtonTemplate extends JButton{

    public ButtonTemplate(){
        super();
        init();
    }

    private void init() {
        setForeground(Design.FG_COLOR);
        setBackground(Design.BG_COLOR);
        setSize(Design.PREFERED_SIZE);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Design.OVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Design.BG_COLOR);
            }
        });
    }

    public ButtonTemplate(String texte){
        super(texte);
        init();
    }
}
