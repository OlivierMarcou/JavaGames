package com.oodrive.omnikles.cryptodoc.swing.component;

import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.DepositStatus;
import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.pojo.ExchangeDocumentState;
import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by olivier on 28/03/17.
 */
public class DepositFilePanel extends JPanel{

    private SslConnexionService ssl = SslConnexionService.getInstance();
    List<DepositStatus> depositStatuses = ssl.getDepositStatusesWithJSessionId(Configuration.parameters.get("urlReadStatus"), Configuration.parameters.get("sessionid"));

    private String[] nameIds = new String[]{"buyerId","tenderId","phaseId","publicationId","supplierId","documentId"};

    private File file;
    private GridBagConstraints fileConstraints = new GridBagConstraints();
    private JLabel text = new JLabel();
    private JCheckBox check = new JCheckBox();
    private FileLabel labelOpenIcon = null;
    private ImageIcon closeIcon = new ImageIcon(new ImageIcon(DepositFilePanel.this.getClass().getResource("/images/notopen.jpeg")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
    private ImageIcon openIcon =  new ImageIcon(new ImageIcon(DepositFilePanel.this.getClass().getResource("/images/openletter.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public JCheckBox getCheck() {
        return check;
    }

    public void setCheck(JCheckBox check) {
        this.check = check;
    }

    private MouseListener checkedListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && !e.isConsumed()) {
                e.consume();
                labelOpenIcon.setIcon(openIcon);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            check.setSelected(!check.isSelected());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    };

    public DepositFilePanel(File file) {
        this.file = file;
        text.setText(file.getName());
        HashMap<String, Long> idsFile = getIdsFile(file.getName());
        DepositStatus depositStatus = findDepositStatus(idsFile);
        setLayout(new GridBagLayout());
        labelOpenIcon = new FileLabel("", file);
        if(depositStatus.getExchangeDocumentState().equals(ExchangeDocumentState.CLOSE)) {
            labelOpenIcon.setIcon(closeIcon);
        }else{
            labelOpenIcon.setIcon(openIcon);
        }
        labelOpenIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        setBackground(Design.BG_COLOR3);
        setPreferredSize(new Dimension(690, 40));
        setMinimumSize(new Dimension(690, 40));

        text.setBackground(Design.BG_COLOR);
        text.setForeground(Design.BG_COLOR4);
        text.setFont(Design.TEXTFIELD_FONT);
        text.setFont(Design.TEXTFIELD_FONT);

        fileConstraints.fill = GridBagConstraints.NONE;
        fileConstraints.anchor = GridBagConstraints.LINE_START;
        fileConstraints.weightx = 1;
        fileConstraints.weighty = 0;
        fileConstraints.gridx = 0;
        fileConstraints.gridy = 0;
        fileConstraints.gridwidth = 1;
        fileConstraints.insets = new Insets(10, 10, 10, 10);
        add(check, fileConstraints);

        fileConstraints.fill = GridBagConstraints.NONE;
        fileConstraints.anchor = GridBagConstraints.CENTER;
        fileConstraints.weightx = 1;
        fileConstraints.weighty = 0;
        fileConstraints.gridx = 1;
        fileConstraints.gridy = 0;
        fileConstraints.gridwidth = 1;
//            text.setPreferredSize(new Dimension(610, 30));
//            text.setMinimumSize(new Dimension(610, 30));
        fileConstraints.insets = new Insets(10, 10, 10, 10);
        add(text, fileConstraints);

        addMouseListener(checkedListener);

        fileConstraints.fill = GridBagConstraints.NONE;
        fileConstraints.anchor = GridBagConstraints.LINE_END;
        fileConstraints.weightx = 1;
        fileConstraints.weighty = 0;
        fileConstraints.gridx = 2;
        fileConstraints.gridy = 0;
        fileConstraints.gridwidth = 1;
        fileConstraints.insets = new Insets(10, 10, 10, 10);
        add(labelOpenIcon, fileConstraints);
    }

    private DepositStatus findDepositStatus(HashMap<String, Long> idsFile) {
        for(DepositStatus depositStatus:depositStatuses){
            if(depositStatus.getId() == idsFile.get("documentId"))
                return depositStatus;
        }
        return null;
    }

    private HashMap<String, Long> getIdsFile(String filename){
        String line = filename.toLowerCase().substring(filename.indexOf("_")+1,filename.lastIndexOf("."));
        String[] idsStr = line.split("_");
        HashMap<String, Long> ids = new HashMap<>();
        for(int i =0; i < idsStr.length; i++){
            ids.put(nameIds[i], Long.parseLong(idsStr[i]));
        }
        return ids;
    }
}