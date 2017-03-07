package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.Configuration;
import com.oodrive.omnikles.depotclient.pojo.Design;
import com.oodrive.omnikles.depotclient.swing.component.ButtonTemplate;
import com.oodrive.omnikles.depotclient.swing.component.GenaralPanelTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by olivier on 02/02/17.
 */
public class IntroWindow extends JFrame {


    private JLabel paragraphe1 = new JLabel("ND");
    private ButtonTemplate okBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page1.button.ok"));
    private ButtonTemplate annulBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page1.button.annul"));
    private ButtonTemplate activfolderBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page1.button.activfolder"));
    private JTextField activFolderTxt = new JTextField();

    public IntroWindow(){
        setTitle(CryptoDoc.textProperties.getProperty("depot.page1.title"));
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GenaralPanelTemplate panel = new GenaralPanelTemplate(this);

        JPanel centerPanel = panel.getCenterPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        panel.getMyStatusBar().setPagesNumber(4);
        panel.getMyStatusBar().setActualPage(1);

        String texte = CryptoDoc.textProperties.getProperty("depot.page1.paragraphe1");
        texte = texte.replace("<titleProcedure>", Configuration.parameters.get("titleProcedure"));
        texte = texte.replace("<organismName>", Configuration.parameters.get("organismName"));
        paragraphe1.setText(texte);

        paragraphe1.setForeground(Design.FG_COLOR);
        activFolderTxt.setForeground(Design.FG_COLOR);

        paragraphe1.setBackground(Design.BG_COLOR);
        activFolderTxt.setBackground(Design.BG_COLOR);


        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=1;
        c.weighty=1;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        centerPanel.add(paragraphe1, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        centerPanel.add(okBtn, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        centerPanel.add(annulBtn, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        centerPanel.add(activfolderBtn, c);

        activFolderTxt.setText(Configuration.activFolder);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=1;
        c.gridy=2;
        c.gridwidth=1;
        centerPanel.add(activFolderTxt, c);



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
