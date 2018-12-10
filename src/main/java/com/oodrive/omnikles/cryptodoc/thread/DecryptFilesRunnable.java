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
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by olivier on 10/02/17.
 */
public class DecryptFilesRunnable extends Thread{

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

    private int chooser(String msg){
        return JOptionPane.showConfirmDialog(this.parent, msg,
                CryptoDoc.textProperties.getProperty("message.warning.title"), JOptionPane.YES_NO_OPTION);
    }

    @Override
    public void run() {
        errorOpener = null;
        boolean isDecrypted = false;
        parent.getPage3Paragraphe2().setText(CryptoDoc.textProperties.getProperty("open.page3.decrypt"));
        for(int i =0 ; i< selectDeposit.size(); i++){
            if(selectDeposit.get(i).getCheck().isSelected()) {
                Logs.sp("Selected zip :  " + selectDeposit.get(i).getFile().getName());
                if(Configuration.isOkMarches){

                    DepositStatus depositStatus = selectDeposit.get(i).getDepositStatus();
                    File destination = new File(selectDeposit.get(i).getFile().getParent() + File.separator + depositStatus.getNumLot() + File.separator + depositStatus.getSupplierName());
                    int choose = -1;
                    if(destination.exists() && destination.isDirectory())
                        choose = chooser(CryptoDoc.textProperties.getProperty("message.chooser.folder.exist").replace("<depot>", depositStatus.getFilename()));
                    if(choose != 1)
                    {
                        isDecrypted = true;
                        DecryptOkMarchesService dos = DecryptOkMarchesService.getInstance();
                        Logs.sp("Debut decrypt plis ...");

                        String compteRenduFile = dos.makeCr(dos.openEnveloppe(selectDeposit.get(i).getFile(), depositStatus));//a retourner lors de l'update du status du pli ouvert
                        Logs.sp("Compte rendu ... " + compteRenduFile);
                        Logs.sp("Fin decrypt plis ... ");
                        try {
                            List<NameValuePair> params = new ArrayList<>();
                            params.add(new BasicNameValuePair("idcand", "" + depositStatus.getId()));
                            params.add(new BasicNameValuePair("crfile", compteRenduFile));
                            params.add(new BasicNameValuePair("iddossier", Configuration.parameters.get("idDossier")));
                            HashMap<String, Long> ids = Configuration.getIdsFile(selectDeposit.get(i).getFile().getName());
                            String idlot = "0";
                            if(ids.get("idlot") != null){
                                idlot = ""+ids.get("idlot");
                            }
                            params.add(new BasicNameValuePair("idlot",  idlot));
                            ssl.updateExchangeDocumentState(Configuration.parameters.get("urlUpdateStatus"), params);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    isDecrypted = true;
                    KeyPair kp = null;
                    try {
                        kp = aes.getKeyPairWithPrivateKey(certificates.getAlias(),"");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        errorOpener += e1.getMessage()+" <br>";
                    }
                    try {
                        selectDeposit.get(i).initDecryptAction(kp);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        errorOpener +=CryptoDoc.textProperties.getProperty("open.page3.decrypt.fail.text").replace("<filename>", selectDeposit.get(i).getName())+ " \n";
                    } catch (CertificateEncodingException e) {
                        e.printStackTrace();
                        errorOpener += e.getMessage()+" <br>";
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        errorOpener += e.getMessage()+" <br>";
                    }
                }
            }
        }
        if(isDecrypted) {
            if (errorOpener != null) {
                error(CryptoDoc.textProperties.getProperty("message.error.open.text").replace("<error>", errorOpener));
            } else {
                parent.getPage3Paragraphe2().setText(CryptoDoc.textProperties.getProperty("open.page3.sending.result.ok"));
            }
            parent.getLoadingIcon().setVisible(false);
        }else{
            this.parent.setScreenNumber(2);
            this.parent.activateScreen();
        }
        this.interrupt();
    }

    private void error(String msg){
        JOptionPane.showMessageDialog(parent, msg,
                CryptoDoc.textProperties.getProperty("message.error.title"), JOptionPane.ERROR_MESSAGE);
    }
}
