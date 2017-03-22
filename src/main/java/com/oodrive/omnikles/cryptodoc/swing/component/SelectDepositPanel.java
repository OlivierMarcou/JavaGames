package com.oodrive.omnikles.cryptodoc.swing.component;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.deposit.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.deposit.pojo.Design;
import com.oodrive.omnikles.cryptodoc.deposit.service.ZipService;
import com.oodrive.omnikles.cryptodoc.swing.window.OpenReceivership;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Created by olivier on 07/02/17.
 */
public class SelectDepositPanel extends JPanel {

    private JScrollPane scrollPane;
    private OpenReceivership parent;
    private ZipService zs = new ZipService();
    private JPanel scrollablePanel = new JPanel();

    public SelectDepositPanel(OpenReceivership parent) {
        this.parent = parent;
        setMinimumSize(new Dimension(600, 280));
        setPreferredSize(new Dimension(600, 280));
        setBackground(Design.BG_COLOR);
        setForeground(Design.FG_COLOR);
        setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
        setBorder(BorderFactory.createCompoundBorder(
                getBorder(),
                Design.TEXTFIELD_BORDER_FACTORY));
        setFont(Design.TEXTFIELD_FONT);

        setLayout(new BorderLayout());
        scrollablePanel.setLayout(new GridBagLayout());
        scrollablePanel.setMinimumSize(new Dimension(600, 280));
        scrollablePanel.setBackground(Design.BG_COLOR);
        add(scrollablePanel, BorderLayout.CENTER);

        scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(20, 380));
        add(scrollPane, BorderLayout.CENTER);


    }

    public void getFilesInfos(File[] contentZipFolder) {
        String texte = CryptoDoc.textProperties.getProperty("open.page2.paragraphe2.vide");
        if (contentZipFolder.length > 0) {
            parent.getOpenBtn().setEnabled(true);
            texte = CryptoDoc.textProperties.getProperty("open.page2.paragraphe2.infos");
            texte = texte.replace("<count>", String.valueOf(contentZipFolder.length));
        } else {
            parent.getOpenBtn().setEnabled(false);
        }
        parent.getInfos().setText(texte);
    }

    public void parseFile(File zipFile) {
        String destinationFolderpath = Configuration.activFolder + File.separator + zipFile.getName().substring(0,zipFile.getName().toLowerCase().lastIndexOf(".zip"));

        zs.unzip(zipFile.getPath(), destinationFolderpath );
        File[] contentZipFolder = new File(destinationFolderpath ).listFiles();
        for (int i = 0; i < contentZipFolder.length; i++) {
            JLabel text = new JLabel(contentZipFolder[i].getName());

            GridBagConstraints fileConstraints = new GridBagConstraints();
            JPanel filePanel = new JPanel();
            filePanel.setLayout(new GridBagLayout());

            FileLabel labelOpenIcon = new FileLabel("", contentZipFolder[i]);
            ImageIcon openIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/images/notopen.jpeg")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            labelOpenIcon.setIcon(openIcon);

            labelOpenIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }
            });

            filePanel.setBackground(Design.BG_COLOR3);
            filePanel.setPreferredSize(new Dimension(690, 40));
            filePanel.setMinimumSize(new Dimension(690, 40));

            text.setBackground(Design.BG_COLOR);
            text.setForeground(Design.BG_COLOR4);
            text.setFont(Design.TEXTFIELD_FONT);
            text.setFont(Design.TEXTFIELD_FONT);

            JCheckBox check = new JCheckBox();
            fileConstraints.fill = GridBagConstraints.NONE;
            fileConstraints.anchor = GridBagConstraints.LINE_START;
            fileConstraints.weightx = 1;
            fileConstraints.weighty = 0;
            fileConstraints.gridx = 0;
            fileConstraints.gridy = 0;
            fileConstraints.gridwidth = 1;
            fileConstraints.insets = new Insets(10, 10, 10, 10);
            filePanel.add(check, fileConstraints);

            fileConstraints.fill = GridBagConstraints.NONE;
            fileConstraints.anchor = GridBagConstraints.CENTER;
            fileConstraints.weightx = 1;
            fileConstraints.weighty = 0;
            fileConstraints.gridx = 1;
            fileConstraints.gridy = 0;
            fileConstraints.gridwidth = 1;
//            text.setPreferredSize(new Dimension(610, 30));
//            text.setMinimumSize(new Dimension(610, 30));
            fileConstraints.insets = new Insets(10, 10, 10, 10);
            filePanel.add(text, fileConstraints);

            fileConstraints.fill = GridBagConstraints.NONE;
            fileConstraints.anchor = GridBagConstraints.LINE_END;
            fileConstraints.weightx = 1;
            fileConstraints.weighty = 0;
            fileConstraints.gridx = 2;
            fileConstraints.gridy = 0;
            fileConstraints.gridwidth = 1;
            fileConstraints.insets = new Insets(10, 10, 10, 10);
            filePanel.add(labelOpenIcon, fileConstraints);

            GridBagConstraints listFileContraints = new GridBagConstraints();


            fileConstraints.fill = GridBagConstraints.NONE;
            fileConstraints.anchor = GridBagConstraints.BASELINE;
            fileConstraints.gridx = 0;
            fileConstraints.gridy = i;
            scrollablePanel.add(filePanel, fileConstraints);
            getFilesInfos(contentZipFolder);
        }
        revalidate();
        repaint();
    }
}
