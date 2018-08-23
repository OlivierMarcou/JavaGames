package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.pojo.ProxyConfig;
import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;
import com.oodrive.omnikles.cryptodoc.swing.component.template.ButtonTemplate;
import com.oodrive.omnikles.cryptodoc.swing.component.template.GenaralPanelTemplate;
import org.apache.http.StatusLine;
import org.junit.Assert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by olivier on 02/02/17.
 */
public class ProxyWindow extends JFrame {

    private SslConnexionService ssl = SslConnexionService.getInstance();

    private ButtonTemplate okBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("config.button.proxy.ok"), Design.MAX_SIZE);
    private ButtonTemplate annulBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("config.button.proxy.annuler"), Design.MAX_SIZE);
    private ButtonTemplate testerBtn = new ButtonTemplate(CryptoDoc.textProperties.getProperty("config.button.proxy.tester"), Design.MAX_SIZE);


    private JLabel hostLbl = new JLabel(CryptoDoc.textProperties.getProperty("config.button.proxy.host"));
    private JTextField hostTxt = new JTextField();
    private JLabel portLbl = new JLabel(CryptoDoc.textProperties.getProperty("config.button.proxy.port"));
    private JTextField portTxt = new JTextField();
    private JLabel authTypeLbl = new JLabel(CryptoDoc.textProperties.getProperty("config.button.proxy.type"));
    private JComboBox<String> authTypeCbx= new JComboBox<String>(new String[]{"","basic","digest","negociate","kerberos","ntlm"});
    private JLabel userLbl = new JLabel(CryptoDoc.textProperties.getProperty("config.button.proxy.user"));
    private JTextField userTxt = new JTextField();
    private JLabel passwordLbl = new JLabel(CryptoDoc.textProperties.getProperty("config.button.proxy.password"));
    private JPasswordField passwordTxt = new JPasswordField();

    private GenaralPanelTemplate panel = new GenaralPanelTemplate(this);
    private JPanel centerPanel = panel.getCenterPanel();
    private JPanel emptyPanel = new JPanel();

    public ProxyWindow(){
        GridBagConstraints c = initDesign();

        hostTxt.setText(Configuration.proxy.getHost());
        portTxt.setText(String.valueOf(Configuration.proxy.getPort()));
        authTypeCbx.setSelectedItem(Configuration.proxy.getAuthenticationType());
        userTxt.setText(Configuration.proxy.getUser());
        passwordTxt.setText(Configuration.proxy.getPassword());

        int line = 0;

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(hostLbl, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=1;
        c.gridy=line;
        c.gridwidth=2;
        c.insets = new Insets(10, 10, 10, -280);
        centerPanel.add(hostTxt, c);

        line++;

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(portLbl, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=1;
        c.gridy=line;
        c.gridwidth=2;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(portTxt, c);

        line++;

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(authTypeLbl, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=1;
        c.gridy=line;
        c.gridwidth=2;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(authTypeCbx, c);

        line++;

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(userLbl, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=1;
        c.gridy=line;
        c.gridwidth=2;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(userTxt, c);

        line++;

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(passwordLbl, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=1;
        c.gridy=line;
        c.gridwidth=2;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(passwordTxt, c);

        line++;

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=0;
        c.gridy=line;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(okBtn, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx=0;
        c.weighty=0;
        c.gridx=1;
        c.gridy=line;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(testerBtn, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.weightx=0;
        c.weighty=0;
        c.gridx=2;
        c.gridy=line;
        c.gridwidth=1;
        c.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(annulBtn, c);

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ProxyConfig proxyConfig = getProxyConfig();
                    CryptoDoc.saveLocalConfiguration(proxyConfig);
                    ProxyWindow.this.dispose();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(ProxyWindow.this,
                            e1.getMessage(),
                            CryptoDoc.textProperties.getProperty("config.button.proxy.bad.title"),
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        testerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = "";
                try {
                    Configuration.proxy = getProxyConfig();
                    StatusLine status = ssl.getResponseHttpGet("https://www.google.fr").getStatusLine();
                    int code = status.getStatusCode();
                    result = status.getReasonPhrase();
                    Assert.assertEquals(200, code);
                    JOptionPane.showConfirmDialog(ProxyWindow.this,
                            CryptoDoc.textProperties.getProperty("config.button.proxy.good.text"),
                            CryptoDoc.textProperties.getProperty("config.button.proxy.good.title"),
                            JOptionPane.CLOSED_OPTION);
                }catch(AssertionError exa)
                {
                    errorMsg(result);
                }
                catch (Exception exc){
                    errorMsg(result);
                }
            }
        });

        annulBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProxyWindow.this.dispose();
            }
        });


        setVisible(true);
    }

    private void errorMsg(String result) {
        JOptionPane.showMessageDialog(ProxyWindow.this,

                CryptoDoc.textProperties.getProperty("config.button.proxy.bad.text")
                        + "<br>" +result,
                CryptoDoc.textProperties.getProperty("config.button.proxy.bad.title"),
                JOptionPane.WARNING_MESSAGE);
    }

    private ProxyConfig getProxyConfig() {
        ProxyConfig proxyConfig = new ProxyConfig();
        if(hostTxt.getText() != null && ! hostTxt.getText().isEmpty())
            proxyConfig.setHost(hostTxt.getText());

        if(portTxt.getText() != null && ! portTxt.getText().isEmpty())
            proxyConfig.setPort(Integer.parseInt(portTxt.getText()));

        if(authTypeCbx.getSelectedItem() != null)
            proxyConfig.setAuthenticationType(authTypeCbx.getSelectedItem().toString());

        if(userTxt.getText() != null)
            proxyConfig.setUser(userTxt.getText());

        if(userTxt.getText() != null)
            proxyConfig.setPassword(new String(passwordTxt.getPassword()));
        return proxyConfig;
    }

    private GridBagConstraints initDesign() {
        setTitle(CryptoDoc.textProperties.getProperty("config.button.proxy.title"));
        Dimension dim = new Dimension(600,600);
        setSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        emptyPanel.setBackground(Design.BG_COLOR);
        emptyPanel.setPreferredSize(Design.CENTERPANEL_CONFIG_PREFERED_SIZE);
        emptyPanel.setMinimumSize(Design.CENTERPANEL_CONFIG_PREFERED_SIZE_EMPTY);

        hostLbl.setBackground(Design.BG_COLOR);
        hostLbl.setForeground(Design.FG_COLOR);
        hostLbl.setPreferredSize(Design.LABEL_SIZE);
        hostLbl.setMinimumSize(Design.LABEL_SIZE);

        portLbl.setBackground(Design.BG_COLOR);
        portLbl.setForeground(Design.FG_COLOR);
        portLbl.setPreferredSize(Design.LABEL_SIZE);
        portLbl.setMinimumSize(Design.LABEL_SIZE);

        authTypeLbl.setBackground(Design.BG_COLOR);
        authTypeLbl.setForeground(Design.FG_COLOR);
        authTypeLbl.setPreferredSize(Design.LABEL_SIZE);
        authTypeLbl.setMinimumSize(Design.LABEL_SIZE);

        userLbl.setBackground(Design.BG_COLOR);
        userLbl.setForeground(Design.FG_COLOR);
        userLbl.setPreferredSize(Design.LABEL_SIZE);
        userLbl.setMinimumSize(Design.LABEL_SIZE);

        passwordLbl.setBackground(Design.BG_COLOR);
        passwordLbl.setForeground(Design.FG_COLOR);
        passwordLbl.setPreferredSize(Design.LABEL_SIZE);
        passwordLbl.setMinimumSize(Design.LABEL_SIZE);

        hostTxt.setBackground(Design.BG_COLOR);
        hostTxt.setForeground(Design.FG_COLOR);
        hostTxt.setPreferredSize(Design.TEXTCONFIG_SIZE);
        hostTxt.setMinimumSize(Design.TEXTCONFIG_SIZE);
        hostTxt.setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
        hostTxt.setBorder(BorderFactory.createCompoundBorder(
                hostTxt.getBorder(),
                Design.TEXTFIELD_BORDER_FACTORY));
        hostTxt.setFont(Design.TEXTFIELD_FONT);

        portTxt.setBackground(Design.BG_COLOR);
        portTxt.setForeground(Design.FG_COLOR);
        portTxt.setPreferredSize(Design.TEXTCONFIG_SIZE);
        portTxt.setMinimumSize(Design.TEXTCONFIG_SIZE);
        portTxt.setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
        portTxt.setBorder(BorderFactory.createCompoundBorder(
                portTxt.getBorder(),
                Design.TEXTFIELD_BORDER_FACTORY));
        portTxt.setFont(Design.TEXTFIELD_FONT);

        userTxt.setBackground(Design.BG_COLOR);
        userTxt.setForeground(Design.FG_COLOR);
        userTxt.setPreferredSize(Design.TEXTCONFIG_SIZE);
        userTxt.setMinimumSize(Design.TEXTCONFIG_SIZE);
        userTxt.setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
        userTxt.setBorder(BorderFactory.createCompoundBorder(
                userTxt.getBorder(),
                Design.TEXTFIELD_BORDER_FACTORY));
        userTxt.setFont(Design.TEXTFIELD_FONT);

        passwordTxt.setBackground(Design.BG_COLOR);
        passwordTxt.setForeground(Design.FG_COLOR);
        passwordTxt.setPreferredSize(Design.TEXTCONFIG_SIZE);
        passwordTxt.setMinimumSize(Design.TEXTCONFIG_SIZE);
        passwordTxt.setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
        passwordTxt.setBorder(BorderFactory.createCompoundBorder(
                passwordTxt.getBorder(),
                Design.TEXTFIELD_BORDER_FACTORY));
        passwordTxt.setFont(Design.TEXTFIELD_FONT);

        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        panel.getMyStatusBar().setPagesNumber(0);
        panel.getMyStatusBar().setActualPage(0);
        return c;
    }

}
