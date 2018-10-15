package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.swing.component.template.SummaryTextTemplate;

import javax.swing.*;
import java.awt.*;

public class SpinnerPanel extends JPanel{

    private ImageIcon spinnerIcon =  new ImageIcon(new ImageIcon(OpenReceivership.class.getResource("/load.gif"))
            .getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
    SummaryTextTemplate label = new SummaryTextTemplate(CryptoDoc.textProperties.getProperty("open.loading.paragraphe1"));
    public SpinnerPanel(){
        setOpaque(false);
        setFocusable(false);
        setLayout(new BorderLayout());
        JLabel loadingIcon = new JLabel();
        loadingIcon.setOpaque(false);
        loadingIcon.setIcon(spinnerIcon);
        add(loadingIcon, BorderLayout.CENTER);
        add(label, BorderLayout.SOUTH);
        setVisible(false);
    }
}
