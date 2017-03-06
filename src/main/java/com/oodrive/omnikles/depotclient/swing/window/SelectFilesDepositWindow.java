package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.Design;
import com.oodrive.omnikles.depotclient.swing.component.InteractiveLabel;
import com.oodrive.omnikles.depotclient.swing.component.SelectFilesPanel;
import com.oodrive.omnikles.depotclient.swing.component.TemplateGenaralPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by olivier on 02/02/17.
 */
public class SelectFilesDepositWindow extends JFrame {

    private JPanel generalPanel = new JPanel();

    private JLabel paragraphe1 = new JLabel(CryptoDoc.textProperties.getProperty("depot.page2.paragraphe1"));
    private JLabel paragraphe2 = new JLabel(CryptoDoc.textProperties.getProperty("depot.page2.paragraphe2"));
    private SelectFilesPanel selectedFilePanel = new SelectFilesPanel(this);
    private JLabel paragraphe3 = new JLabel(CryptoDoc.textProperties.getProperty("depot.page2.paragraphe3"));

    public JButton getOkBtn() {
        return okBtn;
    }

    public void setOkBtn(JButton okBtn) {
        this.okBtn = okBtn;
    }

    private JButton okBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page2.button.ok"));
    private JButton annulBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page2.button.annul"));

    public JLabel getInfos() {
        return infos;
    }

    public void setInfos(JLabel infos) {
        this.infos = infos;
    }

    private JLabel infos = new JLabel(CryptoDoc.textProperties.getProperty("depot.page2.paragraphe2.vide"));

    public SelectFilesDepositWindow(){
        setTitle(CryptoDoc.textProperties.getProperty("depot.page2.title"));
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));

        setLayout(new BorderLayout());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        TemplateGenaralPanel panel = new TemplateGenaralPanel(this);
        JPanel centerPanel = panel.getCenterPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setContentPane(panel);

        paragraphe1.setForeground(Design.FG_COLOR);
        paragraphe1.setBackground(Design.BG_COLOR);
        paragraphe2.setForeground(Design.FG_COLOR);
        paragraphe2.setBackground(Design.BG_COLOR);
        selectedFilePanel.setForeground(Design.FG_COLOR);
        selectedFilePanel.setBackground(Design.BG_COLOR);
        paragraphe3.setForeground(Design.FG_COLOR);
        paragraphe3.setBackground(Design.BG_COLOR);
        infos.setForeground(Design.FG_COLOR);
        infos.setBackground(Design.BG_COLOR);
        okBtn.setForeground(Design.FG_COLOR);
        okBtn.setBackground(Design.BG_COLOR);
        annulBtn.setForeground(Design.FG_COLOR);
        annulBtn.setBackground(Design.BG_COLOR);


        okBtn.setEnabled(false);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        centerPanel.add(paragraphe1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=2;
        centerPanel.add(paragraphe2, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=2;
        centerPanel.add(selectedFilePanel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=2;
        centerPanel.add(paragraphe3, c);

        infos.setMinimumSize(new Dimension(20,20));
        c.fill= GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.SOUTHWEST;
        c.weightx = Design.PREFERED_SIZE.getWidth();
        c.weighty = Design.PREFERED_SIZE.getHeight();
        c.gridx=0;
        c.gridy=4;
        c.gridwidth=2;
        centerPanel.add(infos, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = Design.PREFERED_SIZE.getWidth();
        c.weighty = Design.PREFERED_SIZE.getHeight();
        c.gridx=0;
        c.gridy=5;
        c.gridwidth=1;
        centerPanel.add(okBtn, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = Design.PREFERED_SIZE.getWidth();
        c.weighty = Design.PREFERED_SIZE.getHeight();
        c.gridx=1;
        c.gridy=5;
        c.gridwidth=1;
        centerPanel.add(annulBtn, c);

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<File> files = new ArrayList<>();
                for(Component component:selectedFilePanel.getFilenamesPanel().getComponents())
                    if(component.getClass() == InteractiveLabel.class){
                        File file = null;
                        if(((InteractiveLabel)component).getText() != null){
                            file = new File(((InteractiveLabel)component).getText());
                            if(file != null && file.exists()){
                             files.add(file);
                            }
                        }
                    }
                new Step2Window(files);
                dispose();
            }
        });

        annulBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane d = new JOptionPane();
                int retour = d.showConfirmDialog(getContentPane(), CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.message"),
                        CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.title"), JOptionPane.YES_NO_OPTION);
                if(retour == 0)
                {
                    System.exit(1);
                }
            }
        });
        setVisible(true);
    }

}
