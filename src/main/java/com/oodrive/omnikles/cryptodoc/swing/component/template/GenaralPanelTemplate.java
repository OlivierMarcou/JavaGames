package com.oodrive.omnikles.cryptodoc.swing.component.template;

import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.swing.component.FooterBar;
import com.oodrive.omnikles.cryptodoc.swing.window.SpinnerPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by olivier on 03/03/17.
 */
public class GenaralPanelTemplate extends JPanel {

    private JFrame parent = null;
    private JPanel centerPanel = new JPanel();
    private SpinnerPanel centerPanelSpinner = new SpinnerPanel();
    private FooterBar myStatusBar = null;

    public FooterBar getMyStatusBar() {
        return myStatusBar;
    }

    public void setMyStatusBar(FooterBar myStatusBar) {
        this.myStatusBar = myStatusBar;
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public void setCenterPanel(JPanel centerPanel) {
        this.centerPanel = centerPanel;
    }

    public void setLoading(boolean isLoading){
        if(isLoading){
            centerPanel.setVisible(false);
            centerPanelSpinner.setVisible(true);
        }
        else{
            centerPanelSpinner.setVisible(false);
            centerPanel.setVisible(true);
        }
    }

    public GenaralPanelTemplate(JFrame parent) {
        centerPanelSpinner.setVisible(false);
        setBackground(Design.BG_COLOR);
        setForeground(Design.FG_COLOR);
        centerPanel.setBackground(Design.BG_COLOR);
        centerPanel.setForeground(Design.FG_COLOR);
        centerPanel.setPreferredSize(new Dimension(Design.WIDTH_CENTRAL_PANEL, Design.HEIGHT_CENTRAL_PANEL));
        centerPanel.setMaximumSize(new Dimension(Design.WIDTH_CENTRAL_PANEL, Design.HEIGHT_CENTRAL_PANEL));
        centerPanel.setBorder(new EmptyBorder(Design.CONTENT_MARGIN, Design.CONTENT_MARGIN, Design.CONTENT_MARGIN, Design.CONTENT_MARGIN));
        this.parent = parent;
        myStatusBar = new FooterBar(this.parent);
        setLayout(new BorderLayout());
        JPanel restricSizePanel = new JPanel();

        centerPanel.setPreferredSize(Design.CENTERPANEL_PREFERED_SIZE);
        centerPanel.setSize(Design.CENTERPANEL_PREFERED_SIZE);
        restricSizePanel.setLayout(new GridBagLayout());
        restricSizePanel.setBackground(Design.BG_COLOR);
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        restricSizePanel.add(centerPanel, c);
        restricSizePanel.add(centerPanelSpinner, c);

        add(restricSizePanel, BorderLayout.CENTER);
        add(myStatusBar, BorderLayout.PAGE_END);


    }
}
