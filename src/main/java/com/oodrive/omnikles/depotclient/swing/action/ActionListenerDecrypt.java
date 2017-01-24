package com.oodrive.omnikles.depotclient.swing.action;

import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.service.AESService;
import com.oodrive.omnikles.depotclient.service.CryptoService;
import com.oodrive.omnikles.depotclient.service.SslConnexionService;
import com.oodrive.omnikles.depotclient.swing.component.PinCodeTextField;
import com.oodrive.omnikles.depotclient.swing.component.CertificatsComboBox;
import com.oodrive.omnikles.depotclient.utils.ZipUtils;
import org.bouncycastle.crypto.CryptoException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.zip.ZipFile;

/**
 * Created by olivier on 24/01/17.
 */
public class ActionListenerDecrypt implements ActionListener {

    private String urlCryptedFile;
    private String jSessionId;
    private String filename;

    private CryptoService cs = new CryptoService();

    public ActionListenerDecrypt(String urlCryptedFile, String jSessionId,String filename){
        this.urlCryptedFile = urlCryptedFile;
        this.jSessionId = jSessionId;
        this.filename = filename;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//                if(!txtPassword.getText().trim().isEmpty()){
        SslConnexionService ssl = new SslConnexionService();
        File f = ssl.sslDownloadFile(urlCryptedFile, jSessionId, filename);
        //Initialise la clé privé avec le code pin
        KeyPair kp = null;
        try {
            kp = cs.getKeyPairWithPrivateKey(CertificatsComboBox.selectedKeyPair.getAlias(), PinCodeTextField.pinCode);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        AESService aesService = new AESService();

        byte[] secret = new byte[0];
        try {
            byte[] content  = ZipUtils.getContentFile(new ZipFile(f),"ENVELOPPE.key.p7m");
            if(kp != null) {
                secret = aesService.decodeSecretKeyByCertificat(content, kp);
            }else {
                System.out.println("aucun certificat selectionné." );
            }
        } catch (IOException exx) {
            exx.printStackTrace();
        }
//        if(aesService.secret.getEncoded() != secret)
//            throw new InvalidKeyException("les deux clef ne sont pas indentiques !");

        try {
            aesService.decryptFileWithSecretKey( new File(CryptoDocConfiguration.activFolder
                    + File.separatorChar
                    + CryptoDocConfiguration.FILENAME_CRYPTED_ZIP)
                    , new File(CryptoDocConfiguration.activFolder
                            + File.separatorChar
                            + CryptoDocConfiguration.FILENAME_DECRYPTED_ZIP), secret);
        } catch (NoSuchPaddingException |NoSuchAlgorithmException |InvalidAlgorithmParameterException |InvalidKeyException
                |IOException |BadPaddingException |IllegalBlockSizeException |InvalidParameterSpecException |CryptoException ex) {
            ex.printStackTrace();
        }
    }
}
