package com.oodrive.omnikles.cryptodoc.thread;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.swing.component.DepositFilePanel;
import com.oodrive.omnikles.cryptodoc.swing.window.OpenReceivership;

import javax.swing.*;
import java.util.List;

/**
 * Created by olivier on 10/02/17.
 */
public class DecryptFilesRunnable implements Runnable{

    private AESService aes = AESService.getInstance();

    private String errorOpener = null;
    private List< DepositFilePanel > selectDeposit;
    private KeyPair certificates;
    private OpenReceivership parent;

    public DecryptFilesRunnable(List< DepositFilePanel > selectDeposit, KeyPair certificates, OpenReceivership parent){
        this.parent = parent;
        this.selectDeposit = selectDeposit;
        this.certificates = certificates;
    }

    @Override
    public void run() {
        parent.getPage3Paragraphe2().setText(CryptoDoc.textProperties.getProperty("open.page3.decrypt"));
        for(int i =0 ; i< selectDeposit.size(); i++){
            if(selectDeposit.get(i).getCheck().isSelected()) {
                System.out.println("Selected zip :  " + selectDeposit.get(i).getFile().getName());
                KeyPair kp = null;
                try {
                    kp = aes.getKeyPairWithPrivateKey(certificates.getAlias(),"");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    errorOpener += e1.getMessage()+" \n";
                }
                try {
                    selectDeposit.get(i).decryptAction(kp);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    errorOpener += e2.getMessage()+" \n";
                }
            }
        }
        if(errorOpener != null){
            error(CryptoDoc.textProperties.getProperty("message.error.text"));
        }else{
            parent.getPage3Paragraphe2().setText(CryptoDoc.textProperties.getProperty("open.page3.sending.result.ok"));
        }
        parent.getLoadingIcon().setVisible(false);
    }

    private void error(String msg){
        JOptionPane.showMessageDialog(parent, msg,
                CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
    }
}
