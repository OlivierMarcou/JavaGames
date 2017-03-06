package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.Configuration;
import com.oodrive.omnikles.depotclient.pojo.Design;
import com.oodrive.omnikles.depotclient.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.depotclient.swing.component.TemplateGenaralPanel;
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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        TemplateGenaralPanel panel = new TemplateGenaralPanel(this);
        JPanel centerPanel = panel.getCenterPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        paragraphe1.setForeground(Design.FG_COLOR);
        paragraphe1.setBackground(Design.BG_COLOR);
        animate.setForeground(Design.FG_COLOR);
        animate.setBackground(Design.BG_COLOR);
        information.setForeground(Design.FG_COLOR);
        information.setBackground(Design.BG_COLOR);
        retryBtn.setForeground(Design.FG_COLOR);
        retryBtn.setBackground(Design.BG_COLOR);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        centerPanel.add(paragraphe1, c);

        try {
            animate = new AnimatedProgressBar(getClass().getResource("/progressbar.gif").openStream()) ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = Design.PREFERED_SIZE.getWidth();
        c.weighty = Design.PREFERED_SIZE.getHeight();
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        centerPanel.add(animate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        centerPanel.add(information, c);

        if(Configuration.debug) {
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.weightx = Design.PREFERED_SIZE.getWidth();
            c.weighty = Design.PREFERED_SIZE.getHeight();
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
