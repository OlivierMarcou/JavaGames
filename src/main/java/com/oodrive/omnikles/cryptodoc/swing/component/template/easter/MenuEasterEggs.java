package com.oodrive.omnikles.cryptodoc.swing.component.template.easter;

import com.oodrive.omnikles.cryptodoc.swing.component.template.easter.demin.Demineur;
import com.oodrive.omnikles.cryptodoc.swing.component.template.easter.puissance4.Joueur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by olivier on 27/04/17.
 */
public class MenuEasterEggs extends JFrame{

    private JButton tetris = new JButton("Tetris");
    private JButton p4 = new JButton("Puissance 4 (deux joueurs)");
    private JButton p5 = new JButton("Demineur");

    static public Tetris ts = null;
    static public Joueur game4 = null;
    static public Demineur demineur = null;

    public MenuEasterEggs(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Choose Your Game");
        setSize(400,200);
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx=0;
        c.gridy=0;
        c.gridheight=1;
        c.gridwidth=1;
        add(tetris, c);
        tetris.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ts = new Tetris();
            }
        });

        c.gridx=0;
        c.gridy=1;
        c.gridheight=1;
        c.gridwidth=1;
        add(p4, c);
        p4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game4 = new Joueur();
            }
        });

        c.gridx=0;
        c.gridy=2;
        c.gridheight=1;
        c.gridwidth=1;
        add(p5, c);
        p5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                demineur = new Demineur(16, 30, 99, 3);
            }
        });

        setVisible(true);
    }
}
