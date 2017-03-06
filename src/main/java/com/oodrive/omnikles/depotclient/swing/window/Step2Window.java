package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.Configuration;
import com.oodrive.omnikles.depotclient.pojo.Design;
import com.oodrive.omnikles.depotclient.swing.component.TemplateGenaralPanel;

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

    private JPanel generalPanel = new JPanel();

    private JLabel paragraphe1 = new JLabel();

    private JButton sendBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page3.button.send"));
    private JButton dontSendBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page3.button.dontsend"));
    private java.util.List<File> files;
    private int totalSize = 0;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }


    public Step2Window(java.util.List<File> files){
        this.files = files;

        setTitle(CryptoDoc.textProperties.getProperty("depot.page3.title"));
        String texte = CryptoDoc.textProperties.getProperty("depot.page3.paragraphe1");
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

        panel.getMyStatusBar().setPagesNumber(4);
        panel.getMyStatusBar().setActualPage(3);

        paragraphe1.setForeground(Design.FG_COLOR);
        paragraphe1.setBackground(Design.BG_COLOR);
        sendBtn.setForeground(Design.FG_COLOR);
        sendBtn.setBackground(Design.BG_COLOR);
        dontSendBtn.setForeground(Design.FG_COLOR);
        dontSendBtn.setBackground(Design.BG_COLOR);


        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        centerPanel.add(paragraphe1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = Design.PREFERED_SIZE.getWidth();
        c.weighty = Design.PREFERED_SIZE.getHeight();
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        centerPanel.add(sendBtn, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = Design.PREFERED_SIZE.getWidth();
        c.weighty = Design.PREFERED_SIZE.getHeight();
        c.gridx=1;
        c.gridy=1;
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
