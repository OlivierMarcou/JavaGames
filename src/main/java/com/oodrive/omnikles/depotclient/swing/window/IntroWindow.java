package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by olivier on 02/02/17.
 */
public class IntroWindow extends JFrame {

    private JPanel generalPanel = new JPanel();

    private JLabel paragraphe1 = new JLabel(CryptoDoc.textProperties.getProperty("depot.page1.paragraphe1"));
    private JLabel paragraphe2 = new JLabel(CryptoDoc.textProperties.getProperty("depot.page1.paragraphe2"));
    private JLabel paragraphe3 = new JLabel(CryptoDoc.textProperties.getProperty("depot.page1.paragraphe3"));
    private JButton okBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page1.button.ok"));
    private JButton annulBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page1.button.annul"));

    public IntroWindow(){
        setTitle(CryptoDoc.textProperties.getProperty("depot.page1.title"));
        setVisible(true);
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));

        setLayout(new GridBagLayout());
        generalPanel.setBackground(Color.lightGray);
        generalPanel.setMaximumSize(new Dimension(790, 540));
        generalPanel.setBounds(0,0,600,540);
        setContentPane(generalPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        generalPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=1;
        c.weighty=1;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        generalPanel.add(paragraphe1, c);

        JLabel titleProcedure = new JLabel(CryptoDocConfiguration.parameters.get("titleProcedure"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=2;
        generalPanel.add(titleProcedure, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=2;
        generalPanel.add(paragraphe2, c);

        JLabel organismName = new JLabel(CryptoDocConfiguration.parameters.get("organismName"));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=2;
        generalPanel.add(organismName, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=4;
        c.gridwidth=2;
        generalPanel.add(paragraphe3, c);


        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor= GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=5;
        c.gridwidth=1;
        generalPanel.add(okBtn, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor= GridBagConstraints.NORTHWEST;
        c.gridx=1;
        c.gridy=5;
        c.gridwidth=1;
        generalPanel.add(annulBtn, c);

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new SelectFilesDepositWindow();
            }
        });

        annulBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
    }

}
