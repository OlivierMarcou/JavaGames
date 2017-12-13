package com.oodrive.omnikles.cryptodoc.thread;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.DepositStatus;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.service.AESService;
import com.oodrive.omnikles.cryptodoc.service.DecryptOkMarchesService;
import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;
import com.oodrive.omnikles.cryptodoc.swing.component.DepositFilePanel;
import com.oodrive.omnikles.cryptodoc.swing.window.OpenReceivership;
import com.oodrive.omnikles.cryptodoc.utils.Logs;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.swing.*;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivier on 10/02/17.
 */
public class DecryptFilesRunnable implements Runnable{

    private AESService aes = AESService.getInstance();
    private SslConnexionService ssl = SslConnexionService.getInstance();

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
        errorOpener = null;
        parent.getPage3Paragraphe2().setText(CryptoDoc.textProperties.getProperty("open.page3.decrypt"));
        for(int i =0 ; i< selectDeposit.size(); i++){
            if(selectDeposit.get(i).getCheck().isSelected()) {
                Logs.sp("Selected zip :  " + selectDeposit.get(i).getFile().getName());
                if(Configuration.isOkMarches){
                    DecryptOkMarchesService dos = DecryptOkMarchesService.getInstance();
                    Logs.sp("Debut decrypt plis ...");

                    String compteRenduFile = dos.makeCr(dos.openEnveloppe(selectDeposit.get(i).getFile()));//a retourner lors de l'update du status du pli ouvert
                    Logs.sp("Compte rendu ... " + compteRenduFile);
                    Logs.sp("Fin decrypt plis ... ");
                        DepositStatus depositStatus = selectDeposit.get(i).getDepositStatus();
                    try {
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("idcand", "" + depositStatus.getId()));
                        params.add(new BasicNameValuePair("crfile", compteRenduFile));
                        ssl.updateExchangeDocumentState(Configuration.parameters.get("urlUpdateStatus"), params);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }else{
                    KeyPair kp = null;
                    try {
                        kp = aes.getKeyPairWithPrivateKey(certificates.getAlias(),"");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        errorOpener += e1.getMessage()+" \n";
                    }
                    try {
                        selectDeposit.get(i).initDecryptAction(kp);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        errorOpener +=CryptoDoc.textProperties.getProperty("open.page3.decrypt.fail").replace("<filename>", selectDeposit.get(i).getName())+ " \n";
                    } catch (CertificateEncodingException e) {
                        e.printStackTrace();
                        errorOpener += e.getMessage()+" \n";
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        errorOpener += e.getMessage()+" \n";
                    }
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
