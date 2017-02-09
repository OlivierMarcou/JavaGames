package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.service.CryptoService;
import com.oodrive.omnikles.depotclient.service.SslConnexionService;
import com.oodrive.omnikles.depotclient.utils.ZipUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by olivier on 02/02/17.
 */
public class ZipCryptAndSendWindow extends JFrame {

    private JPanel generalPanel = new JPanel();

    private JLabel paragraphe1 = new JLabel();

    private JLabel information = new JLabel();

    private JButton annulBtn = new JButton(CryptoDoc.textProperties.getProperty("depot.page4.button.annul"));
    private List<File> files;

    private CryptoService cs = new CryptoService();

    public ZipCryptAndSendWindow(List<File> files){
        this.files = files;

        setTitle(CryptoDoc.textProperties.getProperty("depot.page4.title"));
        String texte = CryptoDoc.textProperties.getProperty("depot.page4.paragraphe1");
        texte = texte.replace("<titleProcedure>", CryptoDocConfiguration.parameters.get("titleProcedure"));
        texte = texte.replace("<organismName>", CryptoDocConfiguration.parameters.get("organismName"));
        paragraphe1.setText(texte);
        setSize(800,600);
        setMinimumSize(new Dimension(800, 600));

        setLayout(new BorderLayout());
        generalPanel.setBackground(Color.lightGray);
        generalPanel.setMaximumSize(new Dimension(790, 540));
        generalPanel.setBounds(0,0,600,540);
        setContentPane(generalPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        generalPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        generalPanel.add(paragraphe1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        generalPanel.add(information, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        generalPanel.add(annulBtn, c);

        annulBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane d = new JOptionPane();
                int retour = d.showConfirmDialog(getContentPane(), CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.message"),
                        CryptoDoc.textProperties.getProperty("depot.general.optionpanel.exit.title"), JOptionPane.YES_NO_OPTION);
                if(retour == 0)//yes
                {
                    System.exit(1);
                }
            }
        });

        if(CryptoDocConfiguration.debug) {
            JButton debugButton = new JButton("Ré envoyer");
            debugButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        depot();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.gridx=0;
            c.gridy=3;
            c.gridwidth=1;
            generalPanel.add(debugButton, c);
        }
        setVisible(true);
        try {
            depot();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void depot() throws IOException {
        SslConnexionService ssl = new SslConnexionService();
        java.util.List<String> certificats = ssl.getCertificatsWithJSessionId(CryptoDocConfiguration.parameters.get("urlCertificat"), CryptoDocConfiguration.parameters.get("sessionid"));
        if(certificats == null || certificats.size() <= 0)
            throw new NullPointerException("Aucun certificat trouvé pour : " + CryptoDocConfiguration.parameters.get("urlCertificat"));
        Date now = new Date();
        //Zip tous les fichiers sélectionnés
        File zip = new File(CryptoDocConfiguration.activFolder + File.separatorChar + "deposit"+ now.getTime() +".zip");
        System.out.println("Zip path : " + CryptoDocConfiguration.activFolder + File.separatorChar + "deposit"+ now.getTime() +".zip");
        for( File file: files)
            System.out.println(file.getName());
        ZipUtils.addFilesToNewZip(zip, files);

        System.out.println("zip ok");
        //crypte le zip, créé un fichier .crypt et l'ajoute dans enveloppe.zip
        File enveloppe = cs.crypteByCertificats(zip);

        ssl.sslUploadFile(enveloppe, CryptoDocConfiguration.parameters.get("urlDepot"), CryptoDocConfiguration.parameters.get("sessionid"));
    }

}
