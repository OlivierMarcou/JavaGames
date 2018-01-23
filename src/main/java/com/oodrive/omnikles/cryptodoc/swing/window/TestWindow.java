package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.CryptoTests;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.CryptOkMarchesService;
import com.oodrive.omnikles.cryptodoc.service.DecryptOkMarchesService;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.cryptodoc.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GenaralPanelTemplate;
import com.oodrive.omnikles.cryptodoc.thread.TestRunnable;
import com.oodrive.omnikles.cryptodoc.utils.Logs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Created by olivier on 09/02/17.
 */
public class TestWindow extends JFrame {
    private AnimatedProgressBar progressBar;
    private ButtonTemplate retryBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("depot.page3.button.send"));

    CryptOkMarchesService ck = CryptOkMarchesService.getInstance();
    DecryptOkMarchesService dk = DecryptOkMarchesService.getInstance();
    private File zipFile;
    private File p12;

    public TestWindow(){
        setSize(800,800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GenaralPanelTemplate panel = new GenaralPanelTemplate(this);

        JPanel centerPanel = panel.getCenterPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        panel.getMyStatusBar().setPagesNumber(10);
        panel.getMyStatusBar().setActualPage((int)Math.round(Math.random()*10));

        centerPanel.setSize(600,600);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        try {
            progressBar = new AnimatedProgressBar(getClass().getResource("/progressbar.gif").openStream());
        } catch (Exception e) {
            e.printStackTrace();
            exit(1);
        }
        centerPanel.add(progressBar, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        retryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testUpload();
            }
        });
        centerPanel.add(retryBtn, c);

        ButtonTemplate changeLookBtn = new ButtonTemplate("change");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        centerPanel.add(changeLookBtn, c);

        ButtonTemplate parcourirZipBtn = new ButtonTemplate("choisir un zip");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        centerPanel.add(parcourirZipBtn, c);

        ButtonTemplate parcourirP12Btn = new ButtonTemplate("choisir un p12");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.gridx=1;
        c.gridy=2;
        c.gridwidth=1;
        centerPanel.add(parcourirP12Btn, c);

        ButtonTemplate decryptByP12Btn = new ButtonTemplate("Decrypt NEW");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=1;
        centerPanel.add(decryptByP12Btn, c);

        decryptByP12Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CryptoTests cryptoTests = new CryptoTests();
                try {
                    cryptoTests.decryptNew(zipFile, new KeyPair(p12.getAbsolutePath(), "1234"));
                } catch (KeyStoreException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (CertificateException e1) {
                    e1.printStackTrace();
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                } catch (UnrecoverableKeyException e1) {
                    e1.printStackTrace();
                }
            }
        });

        ButtonTemplate decryptByP12OldBtn = new ButtonTemplate("Decrypt OLD");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.gridx=1;
        c.gridy=3;
        c.gridwidth=1;
        centerPanel.add(decryptByP12OldBtn, c);

        decryptByP12OldBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zipFile =  fileChooser().getAbsoluteFile();
                Logs.spDump(dk.openEnveloppe(zipFile));
            }
        });

        ButtonTemplate cryptByP12Btn = new ButtonTemplate("Crypt NEW");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.gridx=0;
        c.gridy=4;
        c.gridwidth=1;
        centerPanel.add(cryptByP12Btn, c);
        cryptByP12Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CryptoTests cryptoTests = new CryptoTests();
//                try {
//                    cryptoTests.cry(zipFile, new KeyPair(p12.getAbsolutePath(), "1234"));
//                } catch (KeyStoreException e1) {
//                    e1.printStackTrace();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                } catch (CertificateException e1) {
//                    e1.printStackTrace();
//                } catch (NoSuchAlgorithmException e1) {
//                    e1.printStackTrace();
//                } catch (UnrecoverableKeyException e1) {
//                    e1.printStackTrace();
//                }
            }
        });

        ButtonTemplate cryptByP12OldBtn = new ButtonTemplate("Crypt OLD");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.gridx=1;
        c.gridy=4;
        c.gridwidth=1;
        centerPanel.add(cryptByP12OldBtn, c);
        cryptByP12OldBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CryptoTests cryptoTests = new CryptoTests();
                try {
                    KeyPair kp = new KeyPair(p12.getAbsolutePath(), "ok");
                    List<KeyPair> kps = new ArrayList<>();
                    kps.add(kp);
                    String pathCryptedKeyFile = Configuration.activFolder + File.separator + Configuration.FILENAME_CRYPTED_KEYS;
                    byte[] symKey = ck.genereSymKeyFile(pathCryptedKeyFile,kps);
                    ck.cryptFileWithSymKey(symKey, fileChooser().getAbsoluteFile(), pathCryptedKeyFile);
                } catch (KeyStoreException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (CertificateException e1) {
                    e1.printStackTrace();
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                } catch (UnrecoverableKeyException e1) {
                    e1.printStackTrace();
                }
            }
        });

        parcourirZipBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zipFile = fileChooser();
            }
        });

        parcourirP12Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p12 = fileChooser();
            }
        });


        changeLookBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long test = Math.round(Math.random()*3);
                Logs.sp(test);
                CryptoDoc.changeLookAndFeel((int) test, TestWindow.this );
            }
        });
        setVisible(true);
    //    testUpload();
    }

    public void testUpload(){
        TestRunnable test = new TestRunnable();
        test.setProgressBar(progressBar);
        test.file=  fileChooser();
        test.start();
    }


    private File fileChooser() {
        JFileChooser c = new JFileChooser(Configuration.activFolder);
        c.setPreferredSize(new Dimension(600,480));
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
