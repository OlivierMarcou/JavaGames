package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.swing.component.InteractiveLabel;
import com.oodrive.omnikles.depotclient.swing.component.SelectFilesPanel;
import com.oodrive.omnikles.depotclient.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.depotclient.swing.component.template.GenaralPanelTemplate;
import com.oodrive.omnikles.depotclient.swing.component.template.GeneralTextTemplate;
import com.oodrive.omnikles.depotclient.swing.component.template.SummaryTextTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by olivier on 02/02/17.
 */
public class SelectFilesDepositWindow extends JFrame {

    private SummaryTextTemplate paragraphe1 = new SummaryTextTemplate(CryptoDoc.textProperties.getProperty("depot.page2.paragraphe1"));
    private GeneralTextTemplate paragraphe2 = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("depot.page2.paragraphe2"));
    private SelectFilesPanel selectedFilePanel = new SelectFilesPanel(this);
    private GeneralTextTemplate paragraphe3 = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("depot.page2.paragraphe3"));

    public ButtonTemplate getOkBtn() {
        return okBtn;
    }

    private ButtonTemplate okBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page2.button.ok"));
    private ButtonTemplate annulBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page2.button.annul"));

    public GeneralTextTemplate getInfos() {
        return infos;
    }

    private GeneralTextTemplate infos = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("depot.page2.paragraphe2.vide"));

    public SelectFilesDepositWindow(){
        setTitle(CryptoDoc.textProperties.getProperty("depot.page2.title"));
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));

        setLayout(new BorderLayout());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GenaralPanelTemplate panel = new GenaralPanelTemplate(this);
        JPanel centerPanel = panel.getCenterPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        panel.getMyStatusBar().setPagesNumber(4);
        panel.getMyStatusBar().setActualPage(2);

        okBtn.setEnabled(false);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        centerPanel.add(paragraphe1, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=2;
        centerPanel.add(paragraphe2, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=2;
        centerPanel.add(selectedFilePanel, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=2;
        centerPanel.add(paragraphe3, c);

        infos.setMinimumSize(new Dimension(20,20));
        c.fill= GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx=0;
        c.gridy=4;
        c.gridwidth=2;
        centerPanel.add(infos, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx=0;
        c.gridy=5;
        c.gridwidth=1;
        centerPanel.add(okBtn, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.gridx=1;
        c.gridy=5;
        c.gridwidth=1;
        centerPanel.add(annulBtn, c);

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<File> files = new ArrayList<>();
                for(Component component:selectedFilePanel.getFilenamesPanel().getComponents())
                    if(component.getClass() == InteractiveLabel.class){
                        File file = null;
                        if(((InteractiveLabel)component).getText() != null){
                            file = new File(((InteractiveLabel)component).getText());
                            if(file != null && file.exists()){
                             files.add(file);
                            }
                        }
                    }
                new Step2Window(files);
                dispose();
            }
        });

        annulBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane d = new JOptionPane();
                int retour = d.showConfirmDialog(getContentPane(), CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.message"),
                        CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.title"), JOptionPane.YES_NO_OPTION);
                if(retour == 0)
                {
                    System.exit(1);
                }
            }
        });
        setVisible(true);
    }

}
