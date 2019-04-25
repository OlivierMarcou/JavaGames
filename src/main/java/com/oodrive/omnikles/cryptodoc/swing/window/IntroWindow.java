package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GenaralPanelTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.SummaryTextTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by olivier on 02/02/17.
 */
public class IntroWindow extends JFrame {


    private SummaryTextTemplate paragraphe1 = new SummaryTextTemplate("ND");
    private ButtonTemplate okBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page1.button.ok"), Design.MAX_SIZE);
    private ButtonTemplate annulBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page1.button.annul"), Design.MAX_SIZE);
    private ButtonTemplate activfolderBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page1.button.activfolder"), Design.MAX_SIZE);
    private ButtonTemplate proxyConfigBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("config.button.proxy.button"), Design.MAX_SIZE);
    private ButtonTemplate changeLookBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("config.button.proxy.look"), Design.MAX_SIZE);

    private int laf = CryptoDoc.getLaf();
    private JTextField activFolderTxt = new JTextField();

    public IntroWindow(){
        setTitle(CryptoDoc.textProperties.getProperty("depot.page1.title"));
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));
        setMaximumSize(new Dimension(800, 600));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GenaralPanelTemplate panel = new GenaralPanelTemplate(this);

        JPanel centerPanel = panel.getCenterPanel();
//        centerPanel.setBackground(Design.BG_COLOR2);
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Design.BG_COLOR);
        emptyPanel.setPreferredSize(Design.CENTERPANEL_PREFERED_SIZE);
        emptyPanel.setMinimumSize(Design.CENTERPANEL_PREFERED_SIZE_EMPTY);
//        emptyPanel.setSize(Design.CENTERPANEL_PREFERED_SIZE);

        activFolderTxt.setBackground(Design.BG_COLOR);
        activFolderTxt.setForeground(Design.FG_COLOR);
        activFolderTxt.setPreferredSize(Design.TEXTFIELD_SIZE);
        activFolderTxt.setMinimumSize(Design.TEXTFIELD_SIZE);

        activFolderTxt.setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
        activFolderTxt.setBorder(BorderFactory.createCompoundBorder(
                activFolderTxt.getBorder(),
                Design.TEXTFIELD_BORDER_FACTORY));
        activFolderTxt.setFont(Design.TEXTFIELD_FONT);


        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        panel.getMyStatusBar().setPagesNumber(4);
        panel.getMyStatusBar().setActualPage(1);

        String texte = CryptoDoc.textProperties.getProperty("depot.page1.paragraphe1");
        texte = texte.replace("<titleProcedure>", Configuration.parameters.get("titleProcedure"));
        texte = texte.replace("<organismName>", Configuration.parameters.get("organismName"));
        if(Configuration.parameters.get("liblot") != null
                && !Configuration.parameters.get("liblot").isEmpty()
                && !Configuration.parameters.get("liblot").equals("0")
                && !Configuration.parameters.get("liblot").equals("-1")) {
            String paragraphLot = CryptoDoc.textProperties.getProperty("depot.allpages.lot");
            paragraphLot = paragraphLot.replace("<liblot>", (Configuration.parameters.get("liblot") != null ? Configuration.parameters.get("liblot") : ""));
            texte = texte.replace("<lot>", paragraphLot);
        }else
        {
            texte = texte.replace("<lot>", "");
        }
        paragraphe1.setText(texte);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=3;
        c.weighty=0;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=3;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(paragraphe1, c);


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // btn sélectionner le dossier

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=0;
        c.weighty=0;
        c.gridx=2;
        c.gridy=1;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(activfolderBtn, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx=0;
        c.weighty=0;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, -280);
        centerPanel.add(proxyConfigBtn, c);

        if(Boolean.parseBoolean(Configuration.parameters.get("look")) == true){
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.LINE_END;
            c.weightx=0;
            c.weighty=0;
            c.gridx=0;
            c.gridy=1;
            c.gridwidth=1;
            c.insets = new Insets(10, 10, 10, -280);
            centerPanel.add(changeLookBtn, c);

            changeLookBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(laf >= 3)
                        laf=0;
                    else
                        laf++;
                    CryptoDoc.changeLookAndFeel(laf, IntroWindow.this);
                }
            });
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Input
        activFolderTxt.setText(Configuration.activFolder);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=3;
        c.weighty=0;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=3;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(activFolderTxt, c);


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // btn commencer & annuler

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=1;
        c.weighty=0;
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(emptyPanel, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx=1;
        c.weighty=0;
        c.gridx=1;
        c.gridy=3;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, -280);
        centerPanel.add(okBtn, c);


        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx=1;
        c.weighty=0;
        c.gridx=2;
        c.gridy=3;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(annulBtn, c);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=0;
        c.weighty=1;
        c.gridx=0;
        c.gridy=4;
        c.gridwidth=3;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add( emptyPanel, c);



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

        proxyConfigBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProxyWindow proxyWindow = new ProxyWindow();
                proxyWindow.setVisible(true);
            }
        });
        setVisible(true);
    }

}
