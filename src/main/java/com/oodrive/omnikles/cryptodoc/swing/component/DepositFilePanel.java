package com.oodrive.omnikles.cryptodoc.swing.component;

import com.oodrive.omnikles.cryptodoc.deposit.pojo.Design;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * Created by olivier on 28/03/17.
 */
public class DepositFilePanel extends JPanel{

    private File file;
    private GridBagConstraints fileConstraints = new GridBagConstraints();
    private JLabel text = new JLabel(file.getName());
    private JCheckBox check = new JCheckBox();
    private FileLabel labelOpenIcon = null;
    private ImageIcon openIcon = null;

    private MouseListener checkedListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            check.setSelected(!check.isSelected());
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
    };
    public DepositFilePanel(File file) {
        this.file = file;
        setLayout(new GridBagLayout());
        labelOpenIcon = new FileLabel("", file);
        openIcon = new ImageIcon(new ImageIcon(DepositFilePanel.this.getClass().getResource("/images/notopen.jpeg")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        labelOpenIcon.setIcon(openIcon);
        labelOpenIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        setBackground(Design.BG_COLOR3);
        setPreferredSize(new Dimension(690, 40));
        setMinimumSize(new Dimension(690, 40));

        text.setBackground(Design.BG_COLOR);
        text.setForeground(Design.BG_COLOR4);
        text.setFont(Design.TEXTFIELD_FONT);
        text.setFont(Design.TEXTFIELD_FONT);

        fileConstraints.fill = GridBagConstraints.NONE;
        fileConstraints.anchor = GridBagConstraints.LINE_START;
        fileConstraints.weightx = 1;
        fileConstraints.weighty = 0;
        fileConstraints.gridx = 0;
        fileConstraints.gridy = 0;
        fileConstraints.gridwidth = 1;
        fileConstraints.insets = new Insets(10, 10, 10, 10);
        add(check, fileConstraints);

        fileConstraints.fill = GridBagConstraints.NONE;
        fileConstraints.anchor = GridBagConstraints.CENTER;
        fileConstraints.weightx = 1;
        fileConstraints.weighty = 0;
        fileConstraints.gridx = 1;
        fileConstraints.gridy = 0;
        fileConstraints.gridwidth = 1;
//            text.setPreferredSize(new Dimension(610, 30));
//            text.setMinimumSize(new Dimension(610, 30));
        fileConstraints.insets = new Insets(10, 10, 10, 10);
        add(text, fileConstraints);

        text.addMouseListener(checkedListener);

        fileConstraints.fill = GridBagConstraints.NONE;
        fileConstraints.anchor = GridBagConstraints.LINE_END;
        fileConstraints.weightx = 1;
        fileConstraints.weighty = 0;
        fileConstraints.gridx = 2;
        fileConstraints.gridy = 0;
        fileConstraints.gridwidth = 1;
        fileConstraints.insets = new Insets(10, 10, 10, 10);
        add(labelOpenIcon, fileConstraints);
    }
}
