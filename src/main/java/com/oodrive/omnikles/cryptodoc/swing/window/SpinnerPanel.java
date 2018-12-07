package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.swing.component.template.SummaryTextTemplate;

import javax.swing.*;
import java.awt.*;

public class SpinnerPanel extends JPanel{

    private ImageIcon spinnerIcon =  new ImageIcon(new ImageIcon(OpenReceivership.class.getResource("/load.gif"))
            .getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
    SummaryTextTemplate label = new SummaryTextTemplate(CryptoDoc.textProperties.getProperty("open.loading.paragraphe1"));
    public SpinnerPanel(){
        setPreferredSize(new Dimension(200,200));
        setOpaque(false);
        setFocusable(false);
        setLayout(new FlowLayout());

        JLabel loadingIcon = new JLabel();
        loadingIcon.setPreferredSize(new Dimension(64,150));
        loadingIcon.setOpaque(false);
        loadingIcon.setIcon(spinnerIcon);

        add(loadingIcon);
        add(label);

        setVisible(false);
    }
}
