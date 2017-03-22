package com.oodrive.omnikles.cryptodoc.swing.component;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.deposit.pojo.Design;
import com.oodrive.omnikles.cryptodoc.swing.window.OpenReceivership;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivier on 07/02/17.
 */
public class SelectDepositPanel extends JPanel {

    private List<File> files = new ArrayList<>();

    private JPanel filenamesPanel = new JPanel();
    private JScrollPane scrollPane;
    private OpenReceivership parent;

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public SelectDepositPanel(OpenReceivership parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(600, 250));
        setLayout(new GridBagLayout());
        setBackground(Design.BG_COLOR);
        GridBagConstraints c = new GridBagConstraints();

        filenamesPanel.setLayout(new GridBagLayout());
        filenamesPanel.setBackground(Design.BG_COLOR);
        filenamesPanel.setForeground(Design.FG_COLOR);
        filenamesPanel.setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
        filenamesPanel.setBorder(BorderFactory.createCompoundBorder(
                filenamesPanel.getBorder(),
                Design.TEXTFIELD_BORDER_FACTORY));
        filenamesPanel.setFont(Design.TEXTFIELD_FONT);
        filenamesPanel.setMaximumSize(new Dimension(580, 380));
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        add(filenamesPanel, c);

        scrollPane = new JScrollPane(filenamesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(20, 380));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        add(scrollPane, c);

        setMinimumSize(new Dimension(600, 250));

    }

    private void removeSelected(Component component, FileLabel label) {
        files.remove(label.getFile());
        filenamesPanel.remove(component);
        getFilesInfos();
        filenamesPanel.revalidate();
        filenamesPanel.repaint();
    }

    public void getFilesInfos() {
        long totalSize = 0;
        for (File file : files) {
            if (file != null && file.exists()) {
                totalSize += file.length();
            } else
                files.remove(file);
        }

        String texte = CryptoDoc.textProperties.getProperty("open.page2.paragraphe2.vide");
        if (files.size() > 0) {
            parent.getOpenBtn().setEnabled(true);
            texte = CryptoDoc.textProperties.getProperty("open.page2.paragraphe2.infos");
            texte = texte.replace("<count>", String.valueOf(files.size()));
        } else {
            parent.getOpenBtn().setEnabled(false);
        }
        parent.getInfos().setText(texte);
    }

    public void addFileLine(File file) {
        if (file != null && file.exists() && !files.contains(file)) {
            files.add(file);
            JLabel text = new JLabel(file.getName());

            GridBagConstraints constraints = new GridBagConstraints();
            GridBagConstraints fileConstraints = new GridBagConstraints();
            JPanel filePanel = new JPanel();

            FileLabel labelRemoveIcon = new FileLabel("", file);
            ImageIcon removeIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/images/open.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            labelRemoveIcon.setIcon(removeIcon);

            labelRemoveIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JOptionPane d = new JOptionPane();
                    int retour = d.showConfirmDialog(filenamesPanel, CryptoDoc.textProperties.getProperty("depot.page2.optionpanel.deletefile.message"),
                            CryptoDoc.textProperties.getProperty("depot.page2.optionpanel.deletefile.title"), JOptionPane.YES_NO_OPTION);
                    if (retour == 0)//yes
                    {
                        removeSelected(filePanel, labelRemoveIcon);
                    }
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
            fileConstraints.anchor = GridBagConstraints.EAST;
            fileConstraints.weightx = 1;
            fileConstraints.weighty = 0;
            fileConstraints.gridx = 1;
            fileConstraints.gridy = 0;
            fileConstraints.gridwidth = 1;
            text.setPreferredSize(new Dimension(610, 30));
            text.setMinimumSize(new Dimension(610, 30));
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
            filePanel.add(labelRemoveIcon, fileConstraints);

            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.gridx = 0;
            constraints.gridy = files.size()-1;
            constraints.gridwidth = 1;
            constraints.insets = new Insets(5, 10, 5, 10);

            filenamesPanel.add(filePanel, constraints);
            getFilesInfos();
            filenamesPanel.revalidate();
            filenamesPanel.repaint();
        }
    }
}
