package com.oodrive.omnikles.cryptodoc.swing.component.template;

import com.oodrive.omnikles.cryptodoc.pojo.Design;

import javax.swing.*;

/**
 * Created by olivier on 07/03/17.
 */
public class SummaryTextTemplate extends JLabel {


    public SummaryTextTemplate(String text){
        super(text);
        init();
    }

    public SummaryTextTemplate(){
        super();
        init();
    }

    private void init(){
        setFont(Design.SUMMARY_FONT);
        setForeground(Design.FG_COLOR);
        setBackground(Design.BG_COLOR);
    }
}
