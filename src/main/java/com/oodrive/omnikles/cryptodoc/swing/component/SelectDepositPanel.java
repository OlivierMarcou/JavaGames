package com.oodrive.omnikles.cryptodoc.swing.component;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.DepositStatus;
import com.oodrive.omnikles.cryptodoc.pojo.Design;
import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;
import com.oodrive.omnikles.cryptodoc.service.ZipService;
import com.oodrive.omnikles.cryptodoc.swing.window.OpenReceivership;
import org.apache.http.ConnectionClosedException;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

/**
 * Created by olivier on 07/02/17.
 */
public class SelectDepositPanel extends JPanel {

    private SslConnexionService ssl = SslConnexionService.getInstance();
    private HashMap<Long, DepositStatus> depositStatuses = null;
    private String[] nameIds = new String[]{"buyerId","tenderId","phaseId","publicationId","supplierId","documentId"};
    private JScrollPane scrollPane;
    private OpenReceivership parent;
    private ZipService zs = ZipService.getInstance();
    private JPanel scrollablePanel = new JPanel();

    public JPanel getScrollablePanel() {
        return scrollablePanel;
    }

    public SelectDepositPanel(OpenReceivership parent) {
        this.parent = parent;
        setMinimumSize(new Dimension(600, 280));
        setPreferredSize(new Dimension(600, 280));
        setBackground(Design.BG_COLOR);
        setForeground(Design.FG_COLOR);
        setBorder(BorderFactory.createLineBorder(Design.FG_COLOR));
        setBorder(BorderFactory.createCompoundBorder(
                getBorder(),
                Design.TEXTFIELD_BORDER_FACTORY));
        setFont(Design.TEXTFIELD_FONT);

        setLayout(new BorderLayout());
        scrollablePanel.setLayout(new GridBagLayout());
        scrollablePanel.setMinimumSize(new Dimension(600, 280));
        scrollablePanel.setBackground(Design.BG_COLOR);
        add(scrollablePanel, BorderLayout.CENTER);

        scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(20, 380));
        add(scrollPane, BorderLayout.CENTER);


    }

    public void getFilesInfos(File[] contentZipFolder) {
        String texte = CryptoDoc.textProperties.getProperty("open.page2.paragraphe2.vide");
        if (contentZipFolder.length > 0) {
            parent.getOpenBtn().setEnabled(true);
            texte = CryptoDoc.textProperties.getProperty("open.page2.paragraphe2.infos");
            texte = texte.replace("<count>", String.valueOf(contentZipFolder.length));
        } else {
            parent.getOpenBtn().setEnabled(false);
        }
        parent.getInfos().setText(texte);
    }

    public void parseFile(File zipFile) {
        try {
            depositStatuses = null;
            depositStatuses = ssl.getDepositStatusesWithJSessionId(Configuration.parameters.get("urlReadStatus"));
        } catch (JSONException e) {
            e.printStackTrace();
            error(CryptoDoc.textProperties.getProperty("message.error.text"));
        } catch (ConnectionClosedException e) {
            e.printStackTrace();
            error(CryptoDoc.textProperties.getProperty("message.error.text") + e.getMessage());
        }
        Configuration.destinationFolderPath = zipFile.getPath().substring(0,zipFile.getPath().toLowerCase().lastIndexOf(".zip"));
        zs.unzip(zipFile.getPath(), Configuration.destinationFolderPath , true );
        File[] contentZipFolder = new File(Configuration.destinationFolderPath).listFiles();
        for(Component component:scrollablePanel.getComponents()){
            scrollablePanel.remove(component);
        }
        parent.getInfos().setText(CryptoDoc.textProperties.getProperty("open.page2.paragraphe2.vide"));
        for (int i = 0; i < contentZipFolder.length; i++) {
            try {
                getZipLinePanel(contentZipFolder[i], i);
            } catch (Exception e) {
                e.printStackTrace();
                error( CryptoDoc.textProperties.getProperty("message.error.text"));
                return;
            }
            getFilesInfos(contentZipFolder);
        }
        revalidate();
        repaint();
    }

    private void getZipLinePanel(File file, int indexLine) throws JSONException, NumberFormatException {
        HashMap<String, Long> ids = getIdsFile(file.getName());
        DepositFilePanel filePanel = null;
        if(depositStatuses != null && depositStatuses.size() > 0){
            filePanel = new DepositFilePanel(file, depositStatuses.get(ids.get("documentId")));
        }else{
            filePanel = new DepositFilePanel(file, null);
        }
        GridBagConstraints listFileContraints = new GridBagConstraints();
        listFileContraints.fill = GridBagConstraints.HORIZONTAL;
        listFileContraints.anchor = GridBagConstraints.BASELINE;
        listFileContraints.gridx = 0;
        listFileContraints.gridy = indexLine;
        scrollablePanel.add(filePanel, listFileContraints);
    }

    private void error(String msg){
        JOptionPane.showMessageDialog(this, msg,
                CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
    }

    private HashMap<String, Long> getIdsFile(String filename) throws NumberFormatException{
        String line = filename.toLowerCase().substring(filename.indexOf("_")+1,filename.lastIndexOf("."));
        String[] idsStr = line.split("_");
        HashMap<String, Long> ids = new HashMap<>();
        for(int i =0; i < idsStr.length; i++){
            if(idsStr[i] != null){
                long id = Long.parseLong(idsStr[i]);
                ids.put(nameIds[i], id);
            }
        }
        return ids;
    }
}
