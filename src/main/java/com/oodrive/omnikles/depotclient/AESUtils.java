package com.oodrive.omnikles.depotclient;

import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;

import java.io.*;
import java.lang.reflect.Field;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Collection;

/**
 * Created by olivier on 14/11/16.
 */
public class AESUtils {

    public static File encrypt(File file, X509Certificate certificat64cer) {
        try {
            Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        } catch (Exception ex) {
        }
        String resultat = null;
        try {
            // Chargement du fichier à chiffrer
            byte[] buffer = new byte[(int)file.length()];
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            in.readFully(buffer);
            in.close();
            // Chiffrement du document
            CMSEnvelopedDataGenerator gen = new CMSEnvelopedDataGenerator();
            // La variable cert correspond au certificat du destinataire
            // La clé publique de ce certificat servira à chiffrer la clé symétrique
            gen.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(certificat64cer).setProvider("BC"));
            // Choix de l'algorithme à clé symétrique pour chiffrer le document.
            // AES est un standard. Vous pouvez donc l'utiliser sans crainte.
            // Il faut savoir qu'en france la taille maximum autorisée est de 128
            // bits pour les clés symétriques (ou clés secrètes)
            CMSTypedData msg     = new CMSProcessableByteArray(buffer);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            CMSEnvelopedData envData = gen.generate(msg, new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES256_CBC)
                    .setProvider("BC").build());
            System.out.println("Size crypted File: " + envData.getEncoded().length);
            byte[] pkcs7envelopedData = envData.getEncoded();
            // Ecriture du document chiffré
            FileOutputStream envfos = new FileOutputStream(file.getAbsolutePath() + ".crypt");
            envfos.write(pkcs7envelopedData);
            envfos.close();
            return new File(file.getAbsolutePath() + ".crypt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByPk(File file, PrivateKey pk){
        if(file.exists()) {
            byte[] pkcs7envelopedData = new byte[(int) file.length()];
            DataInputStream in = null;
            try {
                in = new DataInputStream(new FileInputStream(file));
                in.readFully(pkcs7envelopedData);
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            CMSEnvelopedData ced = null;
            try {
                ced = new CMSEnvelopedData(pkcs7envelopedData);
            } catch (CMSException e) {
                e.printStackTrace();
            }
            Collection recip = ced.getRecipientInfos().getRecipients();

            KeyTransRecipientInformation rinfo = (KeyTransRecipientInformation)
                    recip.iterator().next();

            byte[] contents = new byte[0];
            try {
                Recipient recipient = new JceKeyTransEnvelopedRecipient(pk).setProvider("BC");
                contents = rinfo.getContent(recipient);
            } catch (CMSException e) {
                e.printStackTrace();
            }

            FileOutputStream envfos = null;
            try {
                envfos = new FileOutputStream("fichier_non_chiffrer");
                envfos.write(contents);
                envfos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contents.toString();
        }else{
            return null;
        }
    }

}
