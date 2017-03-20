package com.oodrive.omnikles.cryptodoc.deposit.swing.component;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.deposit.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.deposit.pojo.Design;
import com.oodrive.omnikles.cryptodoc.deposit.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.deposit.swing.window.SelectFilesDepositWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivier on 07/02/17.
 */
public class SelectFilesPanel extends JPanel {

    private List<File> files = new ArrayList<>();

    private ButtonTemplate parcourirBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page2.button.parcourir"), Design.MAX_SIZE);

    private JPanel filenamesPanel = new JPanel();
    private JScrollPane scrollPane;
    private SelectFilesDepositWindow parent;
    private int lineNumber = 0;

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public SelectFilesPanel(SelectFilesDepositWindow parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(600, 250));
        setLayout(new GridBagLayout());
        setBackground(Design.BG_COLOR);
        GridBagConstraints c = new GridBagConstraints();
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Design.BG_COLOR2);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 10, 0);
        add(parcourirBtn, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 0;
        c.insets = new Insets(0, 0, 0, 0);
        emptyPanel.setPreferredSize(new Dimension(0, 40));
        emptyPanel.setMinimumSize(new Dimension(0, 40));
        add(emptyPanel, c);

        filenamesPanel.setLayout(new GridBagLayout());
//        filenamesPanel.setBackground(Color.decode("#FF2626"));

        filenamesPanel.setBackground(Design.BG_COLOR);
        filenamesPanel.setForeground(Design.FG_COLOR);
//        filenamesPanel.setPreferredSize(Design.TEXTFIELD_SIZE);
//        filenamesPanel.setMinimumSize(Design.TEXTFIELD_SIZE);

        filenamesPanel.setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
        filenamesPanel.setBorder(BorderFactory.createCompoundBorder(
                filenamesPanel.getBorder(),
                Design.TEXTFIELD_BORDER_FACTORY));
        filenamesPanel.setFont(Design.TEXTFIELD_FONT);
        filenamesPanel.setMaximumSize(new Dimension(580, 380));
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        add(filenamesPanel, c);

        scrollPane = new JScrollPane(filenamesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(20, 380));
        c.weightx = 1;
        c.weighty = 1;
        add(scrollPane, c);

        setMinimumSize(new Dimension(600, 250));
        parcourirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser();
            }
        });

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

        String texte = CryptoDoc.textProperties.getProperty("depot.page2.paragraphe2.vide");
        if (files.size() > 0) {
            parent.getOkBtn().setEnabled(true);
            texte = CryptoDoc.textProperties.getProperty("depot.page2.paragraphe2.infos");
            texte = texte.replace("<count>", String.valueOf(files.size()));
            texte = texte.replace("<size>", String.valueOf((totalSize / 1024) / 1024));
            Configuration.totalSizeFiles = totalSize;
        } else {
            parent.getOkBtn().setEnabled(false);
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

            JLabel labelIcon = new JLabel();
//            ImageIcon icon = new ImageIcon(this.getClass().getResource("/images/icon_pdf.png"));
            ImageIcon icon = new ImageIcon(new ImageIcon(this.getClass().getResource(getFileType(file.getName()))).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            labelIcon.setIcon(icon);

            FileLabel labelRemoveIcon = new FileLabel("", file);
//            ImageIcon removeIcon = new ImageIcon(this.getClass().getResource("/images/trash.png"));
            ImageIcon removeIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/images/trash.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
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
//            text.setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
//            text.setBorder(BorderFactory.createCompoundBorder(
//                    text.getBorder(),
//                    Design.TEXTFIELD_BORDER_FACTORY));
            text.setFont(Design.TEXTFIELD_FONT);

            fileConstraints.fill = GridBagConstraints.NONE;
            fileConstraints.anchor = GridBagConstraints.LINE_START;
            fileConstraints.weightx = 1;
            fileConstraints.weighty = 0;
            fileConstraints.gridx = 0;
            fileConstraints.gridy = 0;
            fileConstraints.gridwidth = 1;
            fileConstraints.insets = new Insets(10, 10, 10, 10);
            filePanel.add(labelIcon, fileConstraints);

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

    private File[] fileChooser() {
        JFileChooser c = new JFileChooser(Configuration.activFolder);
        c.setDialogTitle(CryptoDoc.textProperties.getProperty("depot.page2.filechooser.selectfiles"));
        c.setMultiSelectionEnabled(true);
        c.setAcceptAllFileFilterUsed(false);
        int rVal = c.showOpenDialog(SelectFilesPanel.this);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            for (File file : c.getSelectedFiles())
                addFileLine(file);
            return c.getSelectedFiles();
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
        }
        return null;
    }

    private String getFileType(String fileName) {
        System.out.println(fileName);
        String[] filename_array = fileName.split("\\.");
//        System.out.println(filename_array.length);
        String extension = filename_array[filename_array.length - 1];
        switch (extension.toLowerCase()) {
            case "pdf":
                return "/images/icon_pdf.png";
            case "zip":
                return "/images/icon_zip.png";
            case "xls":
                return "/images/icon_table.png";
            case "docx":
            case "doc":
                return "/images/icon_office.png";
            case "jpg":
            case "jpeg":
            case "png":
                return "/images/icon_picture.png";
            default:
                return "/images/icon_file.png";
        }

//

//        return "doc";
    }


}
