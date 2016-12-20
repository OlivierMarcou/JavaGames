package com.oodrive.omnikles.depotclient;

import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.lang.reflect.Field;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;

/**
 * Created by olivier on 14/11/16.
 */
public class AESUtils {

    public static File encrypt(File file, List<X509Certificate> certificats64cer) {
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
            for(X509Certificate x509Certificate:certificats64cer)
                gen.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(x509Certificate).setProvider("BC"));
            // Choix de l'algorithme à clé symétrique pour chiffrer le document.
            // AES est un standard. Vous pouvez donc l'utiliser sans crainte.
            // Il faut savoir qu'en france la taille maximum autorisée est de 128
            // bits pour les clés symétriques (ou clés secrètes)
            CMSTypedData msg     = new CMSProcessableByteArray(buffer);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            CMSEnvelopedData envData = gen.generate(msg, new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC)
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

    public static String decryptMessage(File file, KeyPair keyPair) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte [] decripted  = null;
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
            try {
                if (keyPair.getPrivateKey() != null) {
                    Cipher cipher = Cipher.getInstance("RSA", Security.getProvider("BC"));
                    cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivateKey());
                    cipher.update(pkcs7envelopedData);
                    decripted = cipher.doFinal();
                }
            } catch (InvalidKeyException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (BadPaddingException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            FileOutputStream envfos = null;
            try {
                envfos = new FileOutputStream(System.getProperty("user.home")
                        + File.separatorChar + "fichier_decrypte_" + file.getName());
                envfos.write(decripted);
                envfos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "ok";
        }
        return "echec";
    }
    
    public static String decryptByPk(File file, KeyPair keyPair)
            throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        System.out.println("USE KEY : " + keyPair.toString());
        System.out.println("PK : " + keyPair.getPrivateKey());
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
                Recipient recipient = new JceKeyTransEnvelopedRecipient(keyPair.getPrivateKey()).setProvider("BC");//TODO: ici l'erreur ???
                System.out.println("Encryption Algo OID : ");
                System.out.println(rinfo.getKeyEncryptionAlgOID());
                contents = rinfo.getContent(recipient);
            } catch (CMSException e) {
                e.printStackTrace();
            }

            FileOutputStream envfos = null;
            try {
                envfos = new FileOutputStream(System.getProperty("user.home")
                        + File.separatorChar + "fichier_decrypte_" + file.getName());
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
