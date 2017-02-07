package com.oodrive.omnikles.depotclient.swing.component;

import com.oodrive.omnikles.depotclient.CryptoDoc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by olivier on 07/02/17.
 */
public class SelectFilesPanel extends JPanel{

    private JButton parcourirBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page2.button.parcourir"));
    private JButton deleteBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page2.button.delete"));
    private JPanel filenamesPanel = new JPanel();
    private JScrollPane scrollPane;

    private int lineNumber = 0;

    public SelectFilesPanel(){
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
        filenamesPanel.setBackground(Color.cyan);
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
        filenamesPanel.revalidate();
        filenamesPanel.repaint();
    }

    public int[] getFilesInfos(){
        return new int[]{0,0};
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
            filenamesPanel.revalidate();
            filenamesPanel.repaint();
        }
    }

    private File[] fileChooser() {
        JFileChooser c = new JFileChooser(System.getenv("HOME"));
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
