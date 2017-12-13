package com.oodrive.omnikles.cryptodoc.swing.component;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.*;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;
import com.oodrive.omnikles.cryptodoc.service.ZipService;
import com.oodrive.omnikles.cryptodoc.utils.Logs;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.util.zip.ZipFile;

/**
 * Created by olivier on 28/03/17.
 */
public class DepositFilePanel extends JPanel{

    private AESService aes = AESService.getInstance();
    private ZipService zs = ZipService.getInstance();
    private SslConnexionService ssl = SslConnexionService.getInstance();
    private File file;
    private GridBagConstraints fileConstraints = new GridBagConstraints();
    private JLabel text = new JLabel();
    private JLabel state = new JLabel();
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

    public DepositStatus getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(DepositStatus depositStatus) {
        this.depositStatus = depositStatus;
    }

    private MouseListener checkedListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
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

    private DepositStatus depositStatus;

    public DepositFilePanel(File file, DepositStatus depositStatus) throws JSONException, NumberFormatException {
        this.file = file;
        text.setText(file.getName());
        this.depositStatus = depositStatus;
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(580, 40));
        setMinimumSize(new Dimension(580, 40));

        labelOpenIcon = new FileLabel("", file);
        if(this.depositStatus != null &&
                this.depositStatus.getExchangeDocumentState().equals(ExchangeDocumentState.CLOSE)) {
            labelOpenIcon.setIcon(closeIcon);
        }else{
            labelOpenIcon.setIcon(openIcon);
        }

        setBackground(Design.BG_COLOR3);

        text.setBackground(Design.BG_COLOR);
        text.setForeground(Design.BG_COLOR4);
        text.setFont(Design.TEXTFIELD_FONT);
        text.setFont(Design.TEXTFIELD_FONT);

        fileConstraints.anchor = GridBagConstraints.LINE_START;
        fileConstraints.fill = GridBagConstraints.NONE;
        fileConstraints.gridx = 0;
        fileConstraints.gridy = 0;
        fileConstraints.gridwidth = 1;
        fileConstraints.insets = new Insets(10, 10, 10, 10);
        add(check, fileConstraints);

        fileConstraints.fill = GridBagConstraints.BOTH;
        fileConstraints.gridx = 1;
        fileConstraints.gridy = 0;
        fileConstraints.gridwidth = 1;
        text.setPreferredSize(new Dimension(300, 30));
        text.setMinimumSize(new Dimension(300, 30));
        fileConstraints.insets = new Insets(10, 10, 10, 10);
        add(text, fileConstraints);

        if(depositStatus != null) {
            state.setText(CryptoDoc.textProperties.getProperty(this.depositStatus.getExchangeState().name()));
        }else{
            state.setText(CryptoDoc.textProperties.getProperty(ExchangeState.ERROR.name()));
        }
        fileConstraints.fill = GridBagConstraints.NONE;
        fileConstraints.anchor = GridBagConstraints.CENTER;
        fileConstraints.gridx = 2;
        fileConstraints.gridy = 0;
        fileConstraints.gridwidth = 1;
//            text.setPreferredSize(new Dimension(300, 30));
//            text.setMinimumSize(new Dimension(300, 30));
        fileConstraints.insets = new Insets(10, 10, 10, 10);
        add(state, fileConstraints);

        addMouseListener(checkedListener);

        fileConstraints.fill = GridBagConstraints.NONE;
        fileConstraints.anchor = GridBagConstraints.LINE_END;
        fileConstraints.gridx = 3;
        fileConstraints.gridy = 0;
        fileConstraints.gridwidth = 1;
        fileConstraints.insets = new Insets(10, 10, 10, 10);
        add(labelOpenIcon, fileConstraints);
    }

    public void initDecryptAction(KeyPair kp) throws CertificateEncodingException, IOException {
        String decryptedFoldernameDestination= Configuration.destinationFolderPath
                + File.separatorChar + file.getName().replace(".zip", "");
        File decryptedFileDestination = new File(decryptedFoldernameDestination
                + File.separatorChar
                + depositStatus.getSupplierName().toUpperCase()
                + "_"
                + depositStatus.getSupplierOrganism().toUpperCase()
                + "_"
                + depositStatus.getTenderId()
                + "_"
                + Configuration.FILENAME_DECRYPTED_ZIP);

        File decryptedFolderDestination = new File(decryptedFoldernameDestination);
        int choose = -1;
        if(decryptedFolderDestination.exists() && decryptedFolderDestination.isDirectory())
            choose = chooser(CryptoDoc.textProperties.getProperty("message.chooser.folder.exist").replace("<depot>", file.getName()));
        if(choose != 1)
            decryptAction(kp, decryptedFileDestination, decryptedFoldernameDestination);
    }

    public void decryptAction(KeyPair kp, File decryptedFileDestination, String decryptedFoldernameDestination) throws CertificateEncodingException, IOException {
        byte[] secret = new byte[0];
        try {
            Logs.sp("Zip name :"+file.getName());
            Logs.sp("Zip exist :"+file.exists());
            Logs.sp("Zip path :"+file.getPath());
            Logs.sp("Zip size :"+file.length());
            Logs.sp("FILENAME_CRYPTED_KEYS : " + Configuration.FILENAME_CRYPTED_KEYS);
            byte[] content  = zs.getContentFile(new ZipFile(file), Configuration.FILENAME_CRYPTED_KEYS);
            if(kp != null) {
                Logs.sp("Begin decode sercret key ...");
                secret = aes.decodeJSONSecretKeyByCertificate(content, kp);
                Logs.sp("End decode sercret key ...");
                if(secret == null){
                    error(CryptoDoc.textProperties.getProperty("open.page2.decrypt.secret.fail").replace("<filename>",file.getName()));
//                    return;
                }
            }else {
                Logs.sp("aucun certificat selectionné." );
            }
        } catch (IOException exx) {
            exx.printStackTrace();
            error(CryptoDoc.textProperties.getProperty("message.error.text")+ " " + file.getName());
//            return;
        }
        zs.unzip(file.getPath(), decryptedFoldernameDestination , false);
        File cryptedFile = new File(decryptedFoldernameDestination
                + File.separatorChar
                + Configuration.FILENAME_CRYPTED_ZIP);
        try {
            aes.decryptFileWithSecretKey(cryptedFile, decryptedFileDestination, secret);
        } catch (Exception e1) {
            e1.printStackTrace();
            error(CryptoDoc.textProperties.getProperty("message.error.text")+ " " + file.getName());
        }
        if(depositStatus == null) {
            error(CryptoDoc.textProperties.getProperty("open.page3.decrypt.fail").replace("<filename>", file.getName()));
        }else{
            ssl.updateExchangeDocumentState(depositStatus.getId(), Configuration.parameters.get("urlUpdateStatus"));
        }
    }

    private void error(String msg){
        JOptionPane.showMessageDialog(this, msg,
                CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
    }

    private int chooser(String msg){
        return JOptionPane.showConfirmDialog(this, msg,
                CryptoDoc.textProperties.getProperty("message.warning.title"), JOptionPane.YES_NO_OPTION);
    }

}
