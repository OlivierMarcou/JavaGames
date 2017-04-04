package com.oodrive.omnikles.cryptodoc.swing.component;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.service.ZipService;
import com.oodrive.omnikles.cryptodoc.swing.window.OpenReceivership;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by olivier on 07/02/17.
 */
public class SelectDepositPanel extends JPanel {

    private JScrollPane scrollPane;
    private OpenReceivership parent;
    private ZipService zs = ZipService.getInstance();
    private JPanel scrollablePanel = new JPanel();

    public JPanel getScrollablePanel() {
        return scrollablePanel;
    }

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
        Configuration.destinationFolderPath = zipFile.getPath().substring(0,zipFile.getPath().toLowerCase().lastIndexOf(".zip"));
        zs.unzip(zipFile.getPath(), Configuration.destinationFolderPath , true );
        File[] contentZipFolder = new File(Configuration.destinationFolderPath).listFiles();
        for (int i = 0; i < contentZipFolder.length; i++) {
            getZipLinePanel(contentZipFolder[i], i);
            getFilesInfos(contentZipFolder);
        }
        revalidate();
        repaint();
    }

    private void getZipLinePanel(File file, int indexLine) {
        DepositFilePanel filePanel = new DepositFilePanel(file);
        GridBagConstraints listFileContraints = new GridBagConstraints();
        listFileContraints.fill = GridBagConstraints.NONE;
        listFileContraints.anchor = GridBagConstraints.BASELINE;
        listFileContraints.gridx = 0;
        listFileContraints.gridy = indexLine;
        scrollablePanel.add(filePanel, listFileContraints);
    }

}
