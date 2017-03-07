package com.oodrive.omnikles.depotclient.swing.component;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.Configuration;
import com.oodrive.omnikles.depotclient.swing.window.SelectFilesDepositWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by olivier on 07/02/17.
 */
public class SelectFilesPanel extends JPanel{

    private ButtonTemplate parcourirBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page2.button.parcourir"));
    private ButtonTemplate deleteBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page2.button.delete"));

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
        setPreferredSize(new Dimension(600, 400));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill= GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        parcourirBtn.setPreferredSize(new Dimension(300, 20));
        add(parcourirBtn, c);

        c.fill= GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=0;
        c.gridwidth=1;
        deleteBtn.setPreferredSize(new Dimension(300, 20));
        add(deleteBtn, c);

        filenamesPanel.setLayout(new GridBagLayout());
        filenamesPanel.setBackground(new Color(0xdddddd));
        filenamesPanel.setMaximumSize(new Dimension(580, 380));
        c.fill= GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=2;
        add(filenamesPanel, c);

        scrollPane = new JScrollPane(filenamesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(20,380));
        c.weightx = 1;
        c.weighty = 1;
        add(scrollPane, c);

        setMinimumSize(new Dimension(400, 400));
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

    public void addFileLine(String fileName){
        if(!isAdd(fileName) && !fileName.trim().isEmpty()) {
            InteractiveLabel text = new InteractiveLabel(fileName, this.filenamesPanel);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1;
            constraints.fill= GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.gridx = 0;
            constraints.gridy = lineNumber;
            constraints.gridwidth = 1;
            filenamesPanel.add(text, constraints);
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
                addFileLine(file.getPath());
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

}
