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
import java.io.File;

/**
 * Created by olivier on 07/02/17.
 */
public class SelectFilesPanel extends JPanel{

    private ButtonTemplate parcourirBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page2.button.parcourir"), Design.MAX_SIZE);
    private ButtonTemplate deleteBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page2.button.delete"), Design.MAX_SIZE);

    public JPanel getFilenamesPanel() {
        return filenamesPanel;
    }

    public void setFilenamesPanel(JPanel filenamesPanel) {
        this.filenamesPanel = filenamesPanel;
    }

    private JPanel filenamesPanel = new JPanel();
    private JScrollPane scrollPane;
    private SelectFilesDepositWindow parent;
    private int lineNumber = 0;

    public SelectFilesPanel(SelectFilesDepositWindow parent){
        this.parent = parent;
        setPreferredSize(new Dimension(600, 250));
        setLayout(new GridBagLayout());
        setBackground(Design.BG_COLOR);
        GridBagConstraints c = new GridBagConstraints();
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Design.BG_COLOR2);
//        emptyPanel.setPreferredSize(Design.CENTERPANEL_PREFERED_SIZE);
//        emptyPanel.setMinimumSize(Design.CENTERPANEL_PREFERED_SIZE_EMPTY);

        c.fill= GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
//        parcourirBtn.setPreferredSize(new Dimension(300, 20));
        c.insets = new Insets(0, 10, 10, 0);
        add(parcourirBtn, c);

        c.fill= GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx=1;
        c.gridy=0;
        c.gridwidth=2;
//        deleteBtn.setPreferredSize(new Dimension(300, 20));
        c.insets = new Insets(0, 0, 10, 10);
        add(deleteBtn, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx=1;
        c.weighty=0;
        c.gridx=2;
        c.gridy=0;
        c.gridwidth=0;
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
        c.fill= GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0;
        c.weighty = 1;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=3;
        add(filenamesPanel, c);

        scrollPane = new JScrollPane(filenamesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(20,380));
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
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane d = new JOptionPane();
                int retour = d.showConfirmDialog(filenamesPanel, CryptoDoc.textProperties.getProperty("depot.page2.optionpanel.deletefile.message"),
                        CryptoDoc.textProperties.getProperty("depot.page2.optionpanel.deletefile.title"), JOptionPane.YES_NO_OPTION);
                if(retour == 0)//yes
                {
                    removeSelected();
                }
            }
        });
    }

    private void removeSelected( ){
        for(Component component:filenamesPanel.getComponents()){
            if(component.getClass() == InteractiveLabel.class && ((InteractiveLabel)component).isSelected){
                filenamesPanel.remove(component);
            }
        }
        getFilesInfos();
        filenamesPanel.revalidate();
        filenamesPanel.repaint();
    }

    public void getFilesInfos(){
        long totalSize = 0;
        long count = 0;
        for(Component component:filenamesPanel.getComponents()){
            if(component.getClass() == InteractiveLabel.class){
                File file = null;
                if(((InteractiveLabel)component).getText() != null){
                    file = new File(((InteractiveLabel)component).getText());
                    if(file != null && file.exists()){
                        totalSize += file.length();
                        count ++;
                    }else
                        filenamesPanel.remove(component);
                }
            }
        }
        String texte = CryptoDoc.textProperties.getProperty("depot.page2.paragraphe2.vide");
        if(count > 0) {
            parent.getOkBtn().setEnabled(true);
            texte = CryptoDoc.textProperties.getProperty("depot.page2.paragraphe2.infos");
            texte = texte.replace("<count>", String.valueOf(count));
            texte = texte.replace("<size>", String.valueOf((totalSize/ 1024)/ 1024));
            Configuration.totalSizeFiles = totalSize;
        }else{
            parent.getOkBtn().setEnabled(false);
        }
        parent.getInfos().setText(texte);
    }

    public void addFileLine(File fileName){
        if(!isAdd(fileName.getPath()) && !fileName.getPath().trim().isEmpty()) {
            InteractiveLabel text = new InteractiveLabel(fileName.getName(), this.filenamesPanel);
            GridBagConstraints constraints = new GridBagConstraints();
            GridBagConstraints fileConstraints = new GridBagConstraints();
            JPanel filePanel = new JPanel();

            JLabel labelIcon = new JLabel();
//            ImageIcon icon = new ImageIcon(this.getClass().getResource("/images/icon_pdf.png"));
            ImageIcon icon = new ImageIcon(new ImageIcon(this.getClass().getResource("/images/icon_pdf.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            labelIcon.setIcon(icon);

            JLabel labelRemoveIcon = new JLabel();
//            ImageIcon removeIcon = new ImageIcon(this.getClass().getResource("/images/trash.png"));
            ImageIcon removeIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/images/trash.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            labelRemoveIcon.setIcon(removeIcon);

            filePanel.setBackground(Design.BG_COLOR3);
            filePanel.setPreferredSize(new Dimension(680, 40));
            filePanel.setMinimumSize(new Dimension(680, 40));

            text.setBackground(Design.BG_COLOR);
            text.setForeground(Design.FG_COLOR);
            text.setFont(Design.TEXTFIELD_FONT);
            text.setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
            text.setBorder(BorderFactory.createCompoundBorder(
                    text.getBorder(),
                    Design.TEXTFIELD_BORDER_FACTORY));
            text.setFont(Design.TEXTFIELD_FONT);

            fileConstraints.fill = GridBagConstraints.BOTH;
            fileConstraints.anchor = GridBagConstraints.LINE_START;
            fileConstraints.weightx=1;
            fileConstraints.weighty=0;
            fileConstraints.gridx=0;
            fileConstraints.gridy=0;
            fileConstraints.gridwidth=1;
            fileConstraints.insets = new Insets(10, 10, 10, 10);
            filePanel.add(labelIcon, fileConstraints);

            fileConstraints.fill = GridBagConstraints.NONE;
            fileConstraints.anchor = GridBagConstraints.EAST;
            fileConstraints.weightx=1;
            fileConstraints.weighty=0;
            fileConstraints.gridx=1;
            fileConstraints.gridy=0;
            fileConstraints.gridwidth=1;
            fileConstraints.insets = new Insets(10, 10, 10, 10);
            filePanel.add(text, fileConstraints);

            fileConstraints.fill = GridBagConstraints.NONE;
            fileConstraints.anchor = GridBagConstraints.LINE_END;
            fileConstraints.weightx=1;
            fileConstraints.weighty=0;
            fileConstraints.gridx=2;
            fileConstraints.gridy=0;
            fileConstraints.gridwidth=1;
            fileConstraints.insets = new Insets(10, 10, 10, 10);
            filePanel.add(labelRemoveIcon, fileConstraints);


            constraints.weightx = 1;
            constraints.fill= GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.gridx = 0;
            constraints.gridy = lineNumber;
            constraints.gridwidth = 1;
            constraints.insets = new Insets(5, 10, 5, 10);


            filenamesPanel.add(filePanel, constraints);
            lineNumber++;
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
            for(File file:c.getSelectedFiles())
                addFileLine(file);
            return c.getSelectedFiles();
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
        }
        return null;
    }

    private boolean isAdd(String fileName){
        for(Component component:this.getComponents()){
            if(component.getClass() == InteractiveLabel.class){
                if(((InteractiveLabel)component).getText().equals(fileName))
                    return true;
            }
        }
        return false;
    }

    private String getFileType(String fileName){

        return null;
    }



}
