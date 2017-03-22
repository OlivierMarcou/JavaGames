package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.deposit.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.deposit.pojo.Design;
import com.oodrive.omnikles.cryptodoc.swing.component.SelectDepositPanel;
import com.oodrive.omnikles.cryptodoc.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GenaralPanelTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GeneralTextTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.SummaryTextTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by olivier on 20/03/17.
 */
public class OpenReceivership extends JFrame {


    private SummaryTextTemplate page1Paragraphe1 = new SummaryTextTemplate(CryptoDoc.textProperties.getProperty("open.page1.paragraphe1"));
    private ButtonTemplate selectBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page1.button.select"), Design.MAX_SIZE);
    private GeneralTextTemplate page2Paragraphe1 = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("open.page2.paragraphe1"));

    private ButtonTemplate openBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page1.button.open"), Design.MAX_SIZE);
    private ButtonTemplate backBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page1.button.back"), Design.MAX_SIZE);

    private GeneralTextTemplate infos = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("open.page2.vide"));
    private SelectDepositPanel selectDepositPanel = new SelectDepositPanel(this);

    private GenaralPanelTemplate panel = null;

    public ButtonTemplate getOpenBtn() {
        return openBtn;
    }

    public void setOpenBtn(ButtonTemplate openBtn) {
        this.openBtn = openBtn;
    }

    public GeneralTextTemplate getInfos() {
        return infos;
    }

    public void setInfos(GeneralTextTemplate infos) {
        this.infos = infos;
    }

    public OpenReceivership(){
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));
        setMaximumSize(new Dimension(800, 600));
        panel = new GenaralPanelTemplate(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        JPanel centerPanel = panel.getCenterPanel();
        centerPanel.setMinimumSize(Design.CENTERPANEL_PREFERED_SIZE);

        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        panel.getMyStatusBar().setPagesNumber(3);
        panel.getMyStatusBar().setActualPage(1);

        page2Paragraphe1.setVisible(false);
        selectDepositPanel.setVisible(false);
        infos.setVisible(false);
        openBtn.setVisible(false);
        backBtn.setVisible(false);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        c.insets = new Insets(5, 10, 5, 10);
        centerPanel.add(page1Paragraphe1, c);


        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        centerPanel.add(page2Paragraphe1, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        centerPanel.add(selectBtn, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=2;
        centerPanel.add(selectDepositPanel, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=2;
        centerPanel.add(infos, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=1;
        centerPanel.add(backBtn, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=1;
        c.gridy=3;
        c.gridwidth=1;
        centerPanel.add(openBtn, c);

        initListener();

        setVisible(true);
    }

    private void initListener(){
        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDepositPanel.parseFile(fileChooser());
                page1Paragraphe1.setVisible(false);
                selectBtn.setVisible(false);

                page2Paragraphe1.setVisible(true);
                selectDepositPanel.setVisible(true);
                infos.setVisible(true);
                openBtn.setVisible(true);
                backBtn.setVisible(true);

                panel.getMyStatusBar().setPagesNumber(3);
                panel.getMyStatusBar().setActualPage(2);
            }
        });
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                page1Paragraphe1.setVisible(true);
                selectBtn.setVisible(true);

                page2Paragraphe1.setVisible(false);
                selectDepositPanel.setVisible(false);
                infos.setVisible(false);
                openBtn.setVisible(false);
                backBtn.setVisible(false);

                panel.getMyStatusBar().setPagesNumber(3);
                panel.getMyStatusBar().setActualPage(1);
            }
        });
    }

    private File fileChooser() {
        JFileChooser c = new JFileChooser(Configuration.activFolder);
        c.setDialogTitle(CryptoDoc.textProperties.getProperty("open.page1.filechooser.selectfiles"));
        c.setMultiSelectionEnabled(false);
        c.setAcceptAllFileFilterUsed(false);
        int rVal = c.showOpenDialog(this);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            return c.getSelectedFile();
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
        }
        return null;
    }
}
