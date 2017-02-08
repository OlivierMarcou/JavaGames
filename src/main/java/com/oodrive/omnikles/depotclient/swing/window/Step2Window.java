package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by olivier on 02/02/17.
 */
public class Step2Window extends JFrame {

    private JPanel generalPanel = new JPanel();

    private JLabel paragraphe1 = new JLabel();

    private JButton sendBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page3.button.send"));
    private JButton dontSendBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page3.button.dontsend"));
    private java.util.List<File> files;

    public Step2Window(java.util.List<File> files){
        this.files = files;

        setTitle(CryptoDoc.textProperties.getProperty("depot.page3.title"));
        String texte = CryptoDoc.textProperties.getProperty("depot.page3.paragraphe1");
        texte = texte.replace("<titleProcedure>", CryptoDocConfiguration.parameters.get("titleProcedure"));
        texte = texte.replace("<organismName>", CryptoDocConfiguration.parameters.get("organismName"));
        paragraphe1.setText(texte);
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));

        setLayout(new BorderLayout());
        generalPanel.setBackground(Color.lightGray);
        generalPanel.setMaximumSize(new Dimension(790, 540));
        generalPanel.setBounds(0,0,600,540);
        setContentPane(generalPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        generalPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        generalPanel.add(paragraphe1, c);


        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        generalPanel.add(sendBtn, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        generalPanel.add(dontSendBtn, c);
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ZipCryptAndSendWindow(files);
                dispose();
            }
        });
        dontSendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane d = new JOptionPane();
                int retour = d.showConfirmDialog(getContentPane(), CryptoDoc.textProperties.getProperty("depot.page2.optionpanel.exit.message"),
                        CryptoDoc.textProperties.getProperty("depot.page2.optionpanel.exit.title"), JOptionPane.YES_NO_OPTION);
                if(retour == 0)//yes
                {
                    System.exit(1);
                }
            }
        });
        setVisible(true);
    }

}
