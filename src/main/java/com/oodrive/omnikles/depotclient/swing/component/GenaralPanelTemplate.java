package com.oodrive.omnikles.depotclient.swing.component;

import com.oodrive.omnikles.depotclient.pojo.Design;

import javax.swing.*;
import java.awt.*;

/**
 * Created by olivier on 03/03/17.
 */
public class GenaralPanelTemplate extends JPanel {

    private JFrame parent = null;
    private JPanel topPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private FooterBar myStatusBar = null ;

    public FooterBar getMyStatusBar() {
        return myStatusBar;
    }

    public void setMyStatusBar(FooterBar myStatusBar) {
        this.myStatusBar = myStatusBar;
    }


    public JPanel getTopPanel() {
        return topPanel;
    }

    public void setTopPanel(JPanel topPanel) {
        this.topPanel = topPanel;
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public void setCenterPanel(JPanel centerPanel) {
        this.centerPanel = centerPanel;
    }

    public GenaralPanelTemplate(JFrame parent){
        setBackground(Design.BG_COLOR);
        setForeground(Design.FG_COLOR);
        topPanel.setBackground(Design.BG_COLOR);
        topPanel.setForeground(Design.FG_COLOR);
        centerPanel.setBackground(Design.BG_COLOR);
        centerPanel.setForeground(Design.FG_COLOR);
        this.parent = parent;
        myStatusBar = new FooterBar(this.parent);
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.PAGE_START);
        add(centerPanel, BorderLayout.CENTER);
        add(myStatusBar, BorderLayout.PAGE_END);
    }
}
