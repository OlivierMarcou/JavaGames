package com.oodrive.omnikles.cryptodoc.thread;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.cryptodoc.swing.component.DepositFilePanel;

import javax.swing.*;
import java.util.List;

/**
 * Created by olivier on 10/02/17.
 */
public class DecryptFilesRunnable implements Runnable{

    private AnimatedProgressBar progressBar = null;
    private AESService aes = AESService.getInstance();
    public AnimatedProgressBar getProgressBar() {
        return progressBar;
    }


    public void setProgressBar(AnimatedProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    private String errorOpener = null;
    private List< DepositFilePanel > selectDeposit;
    private KeyPair certificates;

    public DecryptFilesRunnable(List< DepositFilePanel > selectDeposit, AnimatedProgressBar progressBar, KeyPair certificates){
        this.selectDeposit = selectDeposit;
        this.progressBar = progressBar;
        this.certificates = certificates;
    }

    @Override
    public void run() {
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
                    progressBar.setActualIcon(Math.round(((i+1)*100)/selectDeposit.size()));
                }
            }
            if(errorOpener != null) {
                error(CryptoDoc.textProperties.getProperty("message.error.text"));
            }else
                progressBar.finishDecrypt();
    }

    private void error(String msg){
        JOptionPane.showMessageDialog(progressBar, msg,
                CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
    }
}
