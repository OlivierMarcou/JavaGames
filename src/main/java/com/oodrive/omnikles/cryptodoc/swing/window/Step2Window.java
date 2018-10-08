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

import static java.lang.System.exit;

/**
 * Created by olivier on 02/02/17.
 */
public class Step2Window extends JFrame {

    private SummaryTextTemplate paragraphe1 = new SummaryTextTemplate();
    private ButtonTemplate sendBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page3.button.send"), Design.MAX_SIZE);
    private ButtonTemplate dontSendBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page3.button.dontsend"), Design.MAX_SIZE);
    private java.util.List<File> files;
    private int totalSize = 0;

    public Step2Window(final java.util.List<File> files){
        this.files = files;

        setTitle(CryptoDoc.textProperties.getProperty("depot.page3.title"));
        String texte = CryptoDoc.textProperties.getProperty("depot.page3.paragraphe1");
        texte = texte.replace("<titleProcedure>", Configuration.parameters.get("titleProcedure"));
        texte = texte.replace("<organismName>", Configuration.parameters.get("organismName"));
        if(Configuration.parameters.get("liblot") != null
                && !Configuration.parameters.get("liblot").isEmpty()
                && !Configuration.parameters.get("liblot").equals("0")) {
            String paragraphLot = CryptoDoc.textProperties.getProperty("depot.allpages.lot");
            paragraphLot = paragraphLot.replace("<liblot>", (Configuration.parameters.get("liblot") != null ? Configuration.parameters.get("liblot") : ""));
            texte = texte.replace("<lot>", paragraphLot);
        }else
        {
            texte = texte.replace("<lot>", "");
        }
        paragraphe1.setText(texte);
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));

        setLayout(new BorderLayout());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GenaralPanelTemplate panel = new GenaralPanelTemplate(this);
        JPanel centerPanel = panel.getCenterPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        JPanel emptyPanel = new JPanel();
        JPanel emptyPanel2 = new JPanel();
        emptyPanel.setBackground(Design.BG_COLOR);
        emptyPanel2.setBackground(Design.BG_COLOR);
        emptyPanel.setPreferredSize(Design.CENTERPANEL_PREFERED_SIZE);
        emptyPanel.setMinimumSize(Design.CENTERPANEL_PREFERED_SIZE_EMPTY);

        panel.getMyStatusBar().setPagesNumber(4);
        panel.getMyStatusBar().setActualPage(3);

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


        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=3;
        c.weighty=0;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=3;
        c.insets = new Insets(10, 10, 10, 10);
        emptyPanel2.setPreferredSize(new Dimension(700, 50));
        emptyPanel2.setMinimumSize(new Dimension(700, 50));
        centerPanel.add(emptyPanel2, c);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=1;
        c.weighty=0;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        emptyPanel.setPreferredSize(new Dimension(440, 40));
        emptyPanel.setMinimumSize(new Dimension(440, 40));
        centerPanel.add(emptyPanel, c);


        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx=1;
        c.weighty=0;
        c.gridx=1;
        c.gridy=2;
        c.gridwidth=1;
        centerPanel.add(sendBtn, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx=1;
        c.weighty=0;
        c.gridx=2;
        c.gridy=2;
        c.gridwidth=1;
        centerPanel.add(dontSendBtn, c);

        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                ZipCryptAndSendWindow zipCryptAndSendWindow = new ZipCryptAndSendWindow(files);
                dispose();
            }
        });
        dontSendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane d = new JOptionPane();
                int retour = d.showConfirmDialog(getContentPane(), CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.message"),
                        CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.title"), JOptionPane.YES_NO_OPTION);
                if(retour == 0)//yes
                {
                    exit(1);
                }
            }
        });
        setVisible(true);
    }

}
