package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.depotclient.thread.TestRunnable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static java.lang.System.exit;

/**
 * Created by olivier on 09/02/17.
 */
public class TestWindow extends JFrame {
        AnimatedProgressBar label;

    public TestWindow(){
        setSize(800,800);
        setLayout(new BorderLayout());

        try {
            label = new AnimatedProgressBar(getClass().getResource("/progressbar.gif").openStream());
            add(label);
        } catch (Exception e) {
            e.printStackTrace();
            exit(1);
        }
        setVisible(true);gossl();
    }

    public void gossl(){
        TestRunnable test = new TestRunnable();
        test.setProgressBar(label);
        test.file=  fileChooser();
        new Thread(test).start();
    }


    private File fileChooser() {
        JFileChooser c = new JFileChooser(System.getenv("HOME"));
        c.setDialogTitle(CryptoDoc.textProperties.getProperty("depot.page2.filechooser.selectfiles"));
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
