package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.deposit.pojo.Design;
import com.oodrive.omnikles.cryptodoc.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GenaralPanelTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GeneralTextTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.SummaryTextTemplate;

import javax.swing.*;
import java.awt.*;

/**
 * Created by olivier on 20/03/17.
 */
public class OpenReceivership extends JFrame {


    private SummaryTextTemplate paragraphe1 = new SummaryTextTemplate(CryptoDoc.textProperties.getProperty("open.page1.paragraphe1"));
    private ButtonTemplate selectBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page1.button.select"), Design.MAX_SIZE);
    private GeneralTextTemplate activFile = new GeneralTextTemplate();

    public OpenReceivership(){
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));
        setMaximumSize(new Dimension(800, 600));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GenaralPanelTemplate panel = new GenaralPanelTemplate(this);

        JPanel centerPanel = panel.getCenterPanel();
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Design.BG_COLOR);
        emptyPanel.setPreferredSize(Design.CENTERPANEL_PREFERED_SIZE);
        emptyPanel.setMinimumSize(Design.CENTERPANEL_PREFERED_SIZE_EMPTY);


        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        panel.getMyStatusBar().setPagesNumber(3);
        panel.getMyStatusBar().setActualPage(1);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(paragraphe1, c);


        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(selectBtn, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(activFile, c);

        setVisible(true);
    }

}
