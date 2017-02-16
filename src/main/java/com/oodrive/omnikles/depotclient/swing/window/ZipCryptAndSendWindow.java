package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.Configuration;
import com.oodrive.omnikles.depotclient.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.depotclient.thread.DepositFilesRunnable;

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
    private JLabel paragraphe1 = new JLabel();
    private JLabel information = new JLabel();
    private JButton annulBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page4.button.annul"));
    private JButton retryBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page3.button.send"));

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
        generalPanel.setBackground(new Color(0x97abb8));
        generalPanel.setMaximumSize(new Dimension(790, 540));
        generalPanel.setBounds(0,0,600,540);
        setContentPane(generalPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        generalPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        generalPanel.add(paragraphe1, c);

        try {
            animate = new AnimatedProgressBar(getClass().getResource("/progressbar.gif").openStream()) ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        generalPanel.add(animate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        generalPanel.add(information, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=1;
        generalPanel.add(annulBtn, c);

        if(Configuration.debug) {
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.gridx = 0;
            c.gridy = 4;
            c.gridwidth = 1;
            generalPanel.add(retryBtn, c);
        }
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

        annulBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane d = new JOptionPane();
                int retour = d.showConfirmDialog(getContentPane(), CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.message"),
                        CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.title"), JOptionPane.YES_NO_OPTION);
                if(retour == 0)//yes
                {
                    System.exit(1);
                }
            }
        });
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
        File zip = new File(Configuration.activFolder + File.separatorChar + "deposit"+ now.getTime() +".zip");
        System.out.println("Zip path : " + Configuration.activFolder + File.separatorChar + "deposit"+ now.getTime() +".zip");
        for( File file: files)
            System.out.println(file.getName());
        zt.setProgressBar(animate);
        zt.setFiles(files);
        zt.setZip(zip);
        new Thread(zt).start();

    }

}
