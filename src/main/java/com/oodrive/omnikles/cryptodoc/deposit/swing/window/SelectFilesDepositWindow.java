package com.oodrive.omnikles.cryptodoc.deposit.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.deposit.pojo.Design;
import com.oodrive.omnikles.cryptodoc.deposit.swing.component.SelectFilesPanel;
import com.oodrive.omnikles.cryptodoc.deposit.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.deposit.swing.component.template.GenaralPanelTemplate;
import com.oodrive.omnikles.cryptodoc.deposit.swing.component.template.GeneralTextTemplate;
import com.oodrive.omnikles.cryptodoc.deposit.swing.component.template.SummaryTextTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private ButtonTemplate okBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page2.button.ok"), Design.MAX_SIZE);
    private ButtonTemplate annulBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page2.button.annul"), Design.MAX_SIZE);

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
//        centerPanel.setBackground(Design.BG_COLOR2);
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Design.BG_COLOR2);
        emptyPanel.setPreferredSize(Design.CENTERPANEL_PREFERED_SIZE);
        emptyPanel.setMinimumSize(Design.CENTERPANEL_PREFERED_SIZE_EMPTY);

        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        panel.getMyStatusBar().setPagesNumber(4);
        panel.getMyStatusBar().setActualPage(2);

        okBtn.setEnabled(false);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=3;
        c.weighty=0;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=3;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(paragraphe1, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=3;
        c.weighty=0;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=3;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(paragraphe2, c);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=3;
        c.weighty=0;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=3;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(selectedFilePanel, c);
//        centerPanel.add(emptyPanel, c);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=3;
        c.weighty=0;
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=3;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(paragraphe3, c);
//        centerPanel.add(emptyPanel, c);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        infos.setMinimumSize(new Dimension(20,20));
        c.fill= GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=3;
        c.weighty=0;
        c.gridx=0;
        c.gridy=4;
        c.gridwidth=3;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(infos, c);
//        centerPanel.add(emptyPanel, c);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx=1;
        c.weighty=0;
        c.gridx=0;
        c.gridy=6;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        emptyPanel.setPreferredSize(new Dimension(440, 40));
        emptyPanel.setMinimumSize(new Dimension(440, 40));
        centerPanel.add(emptyPanel, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx=1;
        c.weighty=0;
        c.gridx=1;
        c.gridy=6;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(okBtn, c);


        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx=1;
        c.weighty=0;
        c.gridx=2;
        c.gridy=6;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(annulBtn, c);

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Step2Window(selectedFilePanel.getFiles());
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
