package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.cryptodoc.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GenaralPanelTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GeneralTextTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.SummaryTextTemplate;
import com.oodrive.omnikles.cryptodoc.thread.DepositFilesRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by olivier on 02/02/17.
 */
public class ZipCryptAndSendWindow extends JFrame {

    private DepositFilesRunnable zt = new DepositFilesRunnable();
    private JPanel generalPanel = new JPanel();
    private SummaryTextTemplate paragraphe1 = new SummaryTextTemplate();
    private GeneralTextTemplate information = new GeneralTextTemplate();
    private ButtonTemplate retryBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page3.button.send"));

    private List<File> files;
    private AnimatedProgressBar animate = null;

    public ZipCryptAndSendWindow(List<File> files){
        this.files = files;

        setTitle(CryptoDoc.textProperties.getProperty("depot.page4.title"));
        String texte = CryptoDoc.textProperties.getProperty("depot.page4.paragraphe1");
        texte = texte.replace("<titleProcedure>", Configuration.parameters.get("titleProcedure"));
        texte = texte.replace("<organismName>", Configuration.parameters.get("organismName"));
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
        panel.getMyStatusBar().setActualPage(4);

        try {
            animate = new AnimatedProgressBar(getClass().getResource("/progressbar.gif").openStream()) ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        paragraphe1.setPreferredSize(new Dimension(700, 40));
        paragraphe1.setMinimumSize(new Dimension(700, 40));
        centerPanel.add(paragraphe1, c);



        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=1;
        c.weighty=0;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        emptyPanel.setPreferredSize(new Dimension(700, 20));
        emptyPanel.setMinimumSize(new Dimension(700, 20));
        centerPanel.add(emptyPanel, c);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        centerPanel.add(animate, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=1;
        centerPanel.add(information, c);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=1;
        c.weighty=0;
        c.gridx=0;
        c.gridy=4;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        emptyPanel2.setPreferredSize(new Dimension(700, 200));
        emptyPanel2.setMinimumSize(new Dimension(700, 200));
        centerPanel.add(emptyPanel2, c);

        if(Configuration.debug) {
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.CENTER;
            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 1;
            centerPanel.add(retryBtn, c);

            add(retryBtn);
            retryBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        depot();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        animate.setVisible(false);
        setVisible(true);
        try {
            depot();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void depot() throws IOException {
        System.out.println("Zip tous les fichiers selectionnes");
        animate.setVisible(true);
        Date now = new Date();
        String fileName = "deposit"+ now.getTime();
        if(Configuration.isOkMarches)
            fileName = "ENVELOPPE";
        File zip = new File(Configuration.activFolder + File.separatorChar + fileName +".zip");
        System.out.println("Zip path : " + Configuration.activFolder + File.separatorChar + fileName +".zip");
        for( File file: files)
            System.out.println(file.getName());
        zt.setProgressBar(animate);
        zt.setFiles(files);
        zt.setZip(zip);
        new Thread(zt).start();
    }
}
