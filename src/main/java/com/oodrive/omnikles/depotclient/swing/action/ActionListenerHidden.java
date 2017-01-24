package com.oodrive.omnikles.depotclient.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.sun.glass.ui.Cursor.setVisible;

/**
 * Created by olivier on 24/01/17.
 */
public class ActionListenerHidden implements ActionListener {

    public boolean isShowAction() {
        return isShowAction;
    }

    public void setShowAction(boolean showAction) {
        isShowAction = showAction;
    }

    private boolean isShowAction = false;

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(isShowAction);
    }
}
