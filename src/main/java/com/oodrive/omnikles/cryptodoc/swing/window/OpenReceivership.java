package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.service.ZipService;
import com.oodrive.omnikles.cryptodoc.swing.component.CertificatesComboBox;
import com.oodrive.omnikles.cryptodoc.swing.component.DepositFilePanel;
import com.oodrive.omnikles.cryptodoc.swing.component.SelectDepositPanel;
import com.oodrive.omnikles.cryptodoc.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GenaralPanelTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GeneralTextTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.SummaryTextTemplate;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * Created by olivier on 20/03/17.
 */
public class OpenReceivership extends JFrame {


    private SummaryTextTemplate page1Paragraphe1 = new SummaryTextTemplate(CryptoDoc.textProperties.getProperty("open.page1.paragraphe1"));
    private ButtonTemplate selectBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page1.button.select"), Design.MAX_SIZE);
    private GeneralTextTemplate page2Paragraphe1 = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("open.page2.paragraphe1"));

    private ButtonTemplate openBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page1.button.open"), Design.MAX_SIZE);
    private ButtonTemplate backBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page1.button.back"), Design.MAX_SIZE);

    private GeneralTextTemplate infos = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("open.page2.vide"));
    private SelectDepositPanel selectDepositPanel = new SelectDepositPanel(this);

    private CertificatesComboBox listCertificate = new CertificatesComboBox();
    private GeneralTextTemplate lblCertificates = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("open.page2.list.certificate"));
    private GenaralPanelTemplate panel = null;

    public CertificatesComboBox getListCertificate() {
        return listCertificate;
    }

    public void setListCertificate(CertificatesComboBox listCertificate) {
        this.listCertificate = listCertificate;
    }

    public ButtonTemplate getOpenBtn() {
        return openBtn;
    }

    public void setOpenBtn(ButtonTemplate openBtn) {
        this.openBtn = openBtn;
    }

    public GeneralTextTemplate getInfos() {
        return infos;
    }

    public void setInfos(GeneralTextTemplate infos) {
        this.infos = infos;
    }

    public OpenReceivership(){
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));
        setMaximumSize(new Dimension(800, 600));
        panel = new GenaralPanelTemplate(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        JPanel centerPanel = panel.getCenterPanel();
        centerPanel.setMinimumSize(Design.CENTERPANEL_PREFERED_SIZE);

        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        activateScreen(1);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        c.insets = new Insets(5, 10, 5, 10);
        centerPanel.add(page1Paragraphe1, c);


        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        centerPanel.add(page2Paragraphe1, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        centerPanel.add(selectBtn, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=2;
        centerPanel.add(selectDepositPanel, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=2;
        centerPanel.add(infos, c);


        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=1;
        centerPanel.add(lblCertificates, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=1;
        c.gridy=3;
        c.gridwidth=1;
        centerPanel.add(listCertificate, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=4;
        c.gridwidth=1;
        centerPanel.add(backBtn, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=1;
        c.gridy=4;
        c.gridwidth=1;
        centerPanel.add(openBtn, c);

        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDepositPanel.getScrollablePanel().getComponents();
                for(Component component:selectDepositPanel.getScrollablePanel().getComponents())
                    if(component instanceof DepositFilePanel && ((DepositFilePanel) component).getCheck().isSelected()) {
                        System.out.println("hop " + ((DepositFilePanel) component).getFile().getName());
                        decryptAction(((DepositFilePanel) component).getFile());
                    }
            }
        });

        initListener();

        setVisible(true);
    }

    AESService aes = AESService.getInstance();
    ZipService zs = ZipService.getInstance();

    private void decryptAction(File zipFile){
        {
            //Initialise la clé privé avec le code pin
            KeyPair kp = null;
            try {
                kp = aes.getKeyPairWithPrivateKey(
                        ((KeyPair)getListCertificate().getSelectedItem()).getAlias(),
                        "");
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            byte[] secret = new byte[0];
            try {
                System.out.println("Zip name :"+zipFile.getName());
                System.out.println("Zip exist :"+zipFile.exists());
                System.out.println("Zip path :"+zipFile.getPath());
                System.out.println("Zip size :"+zipFile.length());
                System.out.println("FILENAME_CRYPTED_KEYS : " + Configuration.FILENAME_CRYPTED_KEYS);
                byte[] content  = zs.getContentFile(new ZipFile(zipFile), Configuration.FILENAME_CRYPTED_KEYS);
                if(kp != null) {
                    System.out.println("Begin decode sercret key ...");
                    secret = aes.decodeSecretKeyByCertificate(content, kp);
                    System.out.println("End decode sercret key ...");
                }else {
                    System.out.println("aucun certificat selectionné." );
                }
            } catch (IOException exx) {
                exx.printStackTrace();
            }
            zs.unzip(zipFile.getPath(), Configuration.destinationFolderPath, false);
            File cryptedFile = new File(Configuration.destinationFolderPath
                    + File.separatorChar
                    + Configuration.FILENAME_CRYPTED_ZIP);
            try {
                aes.decryptFileWithSecretKey(cryptedFile
                        , new File(Configuration.destinationFolderPath
                                + File.separatorChar
                                + Configuration.FILENAME_DECRYPTED_ZIP), secret);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void initListener(){
        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDepositPanel.parseFile(zipFileChooser());
                activateScreen(2);
            }
        });
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activateScreen(1);
            }
        });
    }

    private File zipFileChooser() {
        JFileChooser c = new JFileChooser(Configuration.activFolder);
        c.setDialogTitle(CryptoDoc.textProperties.getProperty("open.page1.filechooser.selectfiles"));
        c.setMultiSelectionEnabled(false);
        c.setAcceptAllFileFilterUsed(false);
        c.setFileFilter(new FileNameExtensionFilter("Zip File","zip"));
        int rVal = c.showOpenDialog(this);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            return c.getSelectedFile();
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
        }
        return null;
    }

    private void activateScreen(int screennumber){
        boolean one = true;
        boolean two = false;
        switch (screennumber ) {
            case 1:
                one = true;
                two = false;
                break;
            case 2:
                one = false;
                two = true;
                break;
        }
        page1Paragraphe1.setVisible(one);
        selectBtn.setVisible(one);

        page2Paragraphe1.setVisible(two);
        selectDepositPanel.setVisible(two);
        infos.setVisible(two);
        openBtn.setVisible(two);
        backBtn.setVisible(two);
        lblCertificates.setVisible(two);
        listCertificate.setVisible(two);

        panel.getMyStatusBar().setPagesNumber(3);
        panel.getMyStatusBar().setActualPage(screennumber);
    }
}
