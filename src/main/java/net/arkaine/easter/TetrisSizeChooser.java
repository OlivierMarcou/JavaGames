package net.arkaine.easter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by olivier on 27/04/17.
 */
public class TetrisSizeChooser extends JFrame{

    private JButton sBtn = new JButton("Small");
    private JButton nBtn = new JButton("Normal");
    private JButton bBtn = new JButton("Big");

    static public Tetris ts = null;

    public TetrisSizeChooser(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Choose Your Size");
        setSize(400,200);
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx=0;
        c.gridy=0;
        c.gridheight=1;
        c.gridwidth=1;
        add(sBtn, c);
        sBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ts = new Tetris(200,new Point(10,18),35);
            }
        });

        c.gridx=0;
        c.gridy=1;
        c.gridheight=1;
        c.gridwidth=1;
        add(nBtn, c);
        nBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ts = new Tetris(500,new Point(12,24),25);


            }
        });

        c.gridx=0;
        c.gridy=2;
        c.gridheight=1;
        c.gridwidth=1;
        add(bBtn, c);
        bBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ts = new Tetris(2000,new Point(20,40),15);

            }
        });

        setVisible(true);
    }
}
