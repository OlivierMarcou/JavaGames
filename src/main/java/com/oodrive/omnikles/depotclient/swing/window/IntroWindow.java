package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by olivier on 02/02/17.
 */
public class IntroWindow extends JFrame {

    private JPanel generalPanel = new JPanel();

    private JLabel paragraphe1 = new JLabel("ND");
    private JButton okBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page1.button.ok"));
    private JButton annulBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page1.button.annul"));
    private JButton activfolderBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page1.button.activfolder"));
    private JTextField activFolderTxt = new JTextField();

    public IntroWindow(){
        setTitle(CryptoDoc.textProperties.getProperty("depot.page1.title"));
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));

        setLayout(new GridBagLayout());
        generalPanel.setBackground(new Color(0x97abb8));
        generalPanel.setMaximumSize(new Dimension(790, 540));
        generalPanel.setBounds(0,0,600,540);
        setContentPane(generalPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        generalPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        String texte = CryptoDoc.textProperties.getProperty("depot.page1.paragraphe1");
        texte = texte.replace("<titleProcedure>", Configuration.parameters.get("titleProcedure"));
        texte = texte.replace("<organismName>", Configuration.parameters.get("organismName"));
        paragraphe1.setText(texte);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=1;
        c.weighty=1;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        generalPanel.add(paragraphe1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor= GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        generalPanel.add(okBtn, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor= GridBagConstraints.NORTHWEST;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        generalPanel.add(annulBtn, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor= GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        generalPanel.add(activfolderBtn, c);

        activFolderTxt.setText(Configuration.activFolder);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor= GridBagConstraints.NORTHWEST;
        c.gridx=1;
        c.gridy=2;
        c.gridwidth=1;
        generalPanel.add(activFolderTxt, c);

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SelectFilesDepositWindow();
                dispose();
            }
        });

        annulBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane d = new JOptionPane();
                int retour = d.showConfirmDialog(getContentPane(), CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.message"),
                        CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.title"), JOptionPane.YES_NO_OPTION);
                if(retour == 0)
                {
                    System.exit(1);
                }
            }
        });

        activfolderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = null;
                String dir = null;
                JFileChooser c = new JFileChooser(Configuration.activFolder);
                c.setAcceptAllFileFilterUsed(false);
                c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                c.setDialogTitle(CryptoDoc.textProperties.getProperty("depot.page2.filechooser.selectfolder"));
                int rVal = c.showOpenDialog(IntroWindow.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    filename = c.getSelectedFile().getName();
                    dir = c.getCurrentDirectory().toString();
                    Configuration.activFolder = dir + File.separatorChar + filename;
                    activFolderTxt.setText(Configuration.activFolder);
                }
            }
        });

        setVisible(true);
    }

}
