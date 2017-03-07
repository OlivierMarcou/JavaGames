package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.Configuration;
import com.oodrive.omnikles.depotclient.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.depotclient.swing.component.ButtonTemplate;
import com.oodrive.omnikles.depotclient.swing.component.TemplateGenaralPanel;
import com.oodrive.omnikles.depotclient.thread.TestRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static java.lang.System.exit;

/**
 * Created by olivier on 09/02/17.
 */
public class TestWindow extends JFrame {
    private AnimatedProgressBar progressBar;
    private ButtonTemplate retryBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page3.button.send"));

    public TestWindow(){
        setSize(800,800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        TemplateGenaralPanel panel = new TemplateGenaralPanel(this);
        JPanel centerPanel = panel.getCenterPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        panel.getMyStatusBar().setPagesNumber(10);
        panel.getMyStatusBar().setActualPage((int)Math.round(Math.random()*10));
        centerPanel.setSize(600,600);
        try {
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=0;
            c.gridy=0;
            c.gridwidth=1;
            progressBar = new AnimatedProgressBar(getClass().getResource("/progressbar.gif").openStream());
            centerPanel.add(progressBar);
        } catch (Exception e) {
            e.printStackTrace();
            exit(1);
        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        retryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testUpload();
            }
        });
        centerPanel.add(retryBtn);

        ButtonTemplate changeLookBtn = new ButtonTemplate("change");
        c.fill = GridBagConstraints.BOTH;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        changeLookBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long test = Math.round(Math.random()*3);
                System.out.println(test);
                CryptoDoc.changeLookAndFeel((int) test, TestWindow.this );
            }
        });
        centerPanel.add(changeLookBtn);
        add(panel);
        setVisible(true);
        testUpload();
    }

    public void testUpload(){
        TestRunnable test = new TestRunnable();
        test.setProgressBar(progressBar);
        test.file=  fileChooser();
        new Thread(test).start();
    }


    private File fileChooser() {
        JFileChooser c = new JFileChooser(Configuration.activFolder);
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
