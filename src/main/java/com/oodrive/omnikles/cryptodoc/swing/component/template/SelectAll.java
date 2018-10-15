package com.oodrive.omnikles.cryptodoc.swing.component.template;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.swing.component.DepositFilePanel;

import javax.swing.*;
import java.awt.*;

public class SelectAll extends JCheckBox {

    public SelectAll(JPanel listChilds){
        setText(CryptoDoc.textProperties.getProperty("open.page2.all"));
        addActionListener(e -> {
            if(isSelected()){
                setText(CryptoDoc.textProperties.getProperty("open.page2.nothing"));
                for(Component component: listChilds.getComponents())
                    try {
                        ((DepositFilePanel)component).getCheck().setSelected(true);
                    }catch (ClassCastException ex){}
            }else{
                setText(CryptoDoc.textProperties.getProperty("open.page2.all"));
                for(Component component: listChilds.getComponents())
                    try {
                        ((DepositFilePanel)component).getCheck().setSelected(false);
                    }catch (ClassCastException ex){}
            }
        });
    }
}
