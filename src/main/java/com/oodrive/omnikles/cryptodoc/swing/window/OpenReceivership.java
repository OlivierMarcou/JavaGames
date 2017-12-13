package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.swing.component.CertificatesComboBox;
import com.oodrive.omnikles.cryptodoc.swing.component.DepositFilePanel;
import com.oodrive.omnikles.cryptodoc.swing.component.SelectDepositPanel;
import com.oodrive.omnikles.cryptodoc.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GenaralPanelTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GeneralTextTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.SummaryTextTemplate;
import com.oodrive.omnikles.cryptodoc.thread.DecryptFilesRunnable;
import com.oodrive.omnikles.cryptodoc.utils.Logs;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by olivier on 20/03/17.
 */
public class OpenReceivership extends JFrame {

    private AESService aes = AESService.getInstance();
    private SummaryTextTemplate page1Paragraphe1 = new SummaryTextTemplate(CryptoDoc.textProperties.getProperty("open.page1.paragraphe1"));
    private ButtonTemplate selectBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page1.button.select"), Design.MAX_SIZE);
    private GeneralTextTemplate page2Paragraphe1 = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("open.page2.paragraphe1"));
    private int screenNumber = 1;
    private ButtonTemplate openBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page1.button.open"), Design.MAX_SIZE);
    private ButtonTemplate backBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page1.button.back"), Design.MAX_SIZE);

    private GeneralTextTemplate infos = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("open.page2.vide"));

    private SelectDepositPanel selectDepositPanel = new SelectDepositPanel(this);

    private CertificatesComboBox listCertificate = new CertificatesComboBox();
    private GeneralTextTemplate lblCertificates = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("open.page2.list.certificate"));
    private GenaralPanelTemplate panel = null;

    private SummaryTextTemplate page3Paragraphe1 = new SummaryTextTemplate(CryptoDoc.textProperties.getProperty("open.page3.paragraphe1"));

    public GeneralTextTemplate getPage3Paragraphe2() {
        return page3Paragraphe2;
    }

    public void setPage3Paragraphe2(GeneralTextTemplate page3Paragraphe2) {
        this.page3Paragraphe2 = page3Paragraphe2;
    }

    private GeneralTextTemplate page3Paragraphe2 = new GeneralTextTemplate(CryptoDoc.textProperties.getProperty("open.page3.paragraphe2"));
    private ButtonTemplate exitBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("open.page3.button.exit"), Design.MAX_SIZE);

    public JLabel getLoadingIcon() {
        return loadingIcon;
    }

    public void setLoadingIcon(JLabel loadingIcon) {
        this.loadingIcon = loadingIcon;
    }

    private JLabel loadingIcon = new JLabel();

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
        if(Configuration.isOkMarches) {
            lblCertificates.setVisible(false);
            listCertificate.setVisible(false);
        }
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
        loadingIcon.setIcon(new ImageIcon(getClass().getResource("/load.gif")));

        activateScreen();

        int line = 0;

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=1;
        c.insets = new Insets(5, 10, 5, 10);
        centerPanel.add(page1Paragraphe1, c);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=2;
        centerPanel.add(page2Paragraphe1, c);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=2;
        centerPanel.add(page3Paragraphe1, c);

        line++;

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=1;
        centerPanel.add(selectBtn, c);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=2;
        centerPanel.add(selectDepositPanel, c);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        centerPanel.add(loadingIcon, c);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        centerPanel.add(page3Paragraphe2, c);

        line++;

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=1;
        centerPanel.add(lblCertificates, c);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=1;
        c.gridy=line;
        c.gridwidth=1;
        centerPanel.add(listCertificate, c);

        line++;

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=2;
        centerPanel.add(infos, c);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=2;
        centerPanel.add(exitBtn, c);

        line++;

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=1;
        centerPanel.add(backBtn, c);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=1;
        c.gridy=line;
        c.gridwidth=1;
        centerPanel.add(openBtn, c);

        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<DepositFilePanel> selectDeposit = new ArrayList<>();
                for(Component component: selectDepositPanel.getScrollablePanel().getComponents()){
                    if(component instanceof DepositFilePanel && ((DepositFilePanel) component).getCheck().isSelected()) {
                        Logs.sp("Selected file name :  " + ((DepositFilePanel) component).getName());
                        selectDeposit.add((DepositFilePanel)component);
                    }
                }
                if(selectDeposit.size()>0) {
                    screenNumber = 3;
                    activateScreen();
                    Logs.sp("Selected files number :  " + selectDeposit.size());
                    DecryptFilesRunnable decryptFilesRunnable = new DecryptFilesRunnable(selectDeposit,
                            (KeyPair) getListCertificate().getSelectedItem(), OpenReceivership.this);
                    Thread decrypt = new Thread(decryptFilesRunnable);
                    decrypt.start();
                }else
                    OpenReceivership.this.warning(CryptoDoc.textProperties.getProperty("message.warning.emptyselection"));
            }
        });
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        initListener();

        setVisible(true);
    }

    private void error(String msg){
        JOptionPane.showMessageDialog(this, msg,
                CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
    }
    private void warning(String msg){
        JOptionPane.showMessageDialog(this, msg,
                CryptoDoc.textProperties.getProperty("message.warning.title"), JOptionPane.WARNING_MESSAGE);
    }

    private void initListener(){
        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDepositPanel.parseFile(zipFileChooser());
                screenNumber = 2;
                activateScreen();
            }
        });
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screenNumber --;
                activateScreen();
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

    private void activateScreen(){
        boolean one = true;
        boolean two = false;
        boolean three = false;
        switch (screenNumber ) {
            case 1:
                one = true;
                two = false;
                three = false;
                break;
            case 2:
                one = false;
                two = true;
                three = false;
                break;
            case 3:
                one = false;
                two = false;
                three = true ;
                break;
        }

        page1Paragraphe1.setVisible(one);
        selectBtn.setVisible(one);

        page2Paragraphe1.setVisible(two);
        selectDepositPanel.setVisible(two);
        infos.setVisible(two);
        openBtn.setVisible(two);
        backBtn.setVisible((two || three));
        if(!Configuration.isOkMarches) {
            lblCertificates.setVisible(two);
            listCertificate.setVisible(two);
        }

        loadingIcon.setVisible(three);
        page3Paragraphe1.setVisible(three);
        page3Paragraphe2.setVisible(three);
        exitBtn.setVisible(three);

        panel.getMyStatusBar().setPagesNumber(3);
        panel.getMyStatusBar().setActualPage(screenNumber);
    }
}
