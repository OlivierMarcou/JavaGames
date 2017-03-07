package com.oodrive.omnikles.depotclient.swing.component.template;

import com.oodrive.omnikles.depotclient.pojo.Design;

import javax.swing.*;

/**
 * Created by olivier on 07/03/17.
 */
public class GeneralTextTemplate extends JLabel {

    public GeneralTextTemplate(String text){
        super(text);
        init();
    }

    public GeneralTextTemplate(){
        super();
        init();
    }

    private void init(){
        setFont(Design.TEXT_FONT);
        setForeground(Design.FG_COLOR);
        setBackground(Design.BG_COLOR);
    }

}
