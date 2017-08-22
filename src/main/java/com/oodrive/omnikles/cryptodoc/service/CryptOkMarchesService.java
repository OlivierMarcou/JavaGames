package com.oodrive.omnikles.cryptodoc.service;

import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.utils.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CryptOkMarchesService {

    AESService aes = AESService.getInstance();
    private CryptOkMarchesService(){

    }

    public byte[] genereSymKeyFile(String pathsymkey, List<KeyPair> certificates) {
        SecretKey skey = null;
        try {
            skey = generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] raw = skey.getEncoded();
        // crypter cette cle symetrique a l'aide de la cle publique
        byte[] cryptKey = null;
        System.out.println("clé symétrique non cryptee : "+raw.length);
        StringBuffer sb = new StringBuffer();
//        BASE64Encoder encode = new BASE64Encoder();
        for (KeyPair certificate : certificates) {

            sb.append("<ds:EncryptedExchangeKey>\n");
            sb.append("\n<ds:X509Certificate>\n");
            sb.append(certificate.getX509CertificateB64());
            sb.append("\n</ds:X509Certificate>\n");

            //TODO: liste des certif venant de marché
            cryptKey = aes.encrypt(raw, certificate.getCertificate());
	          /* ************************************************************** */
	          /* MBT : correction applet du 19/04 : dans le cas de versions  de Win non a jour */
            if ((cryptKey == null) || (cryptKey.length == 0)) {
                // Erreur au niveau du cryptage
                // dans ce cas, on met la cle symetrique en clair ...
                System.out.println("Attention une erreur est survenue, la clef symétrique n'est pas cryptée !");
                sb.append("\n<ds:EncryptedKey>\n");
                sb.append(new String(Base64.encode(raw)));
                sb.append("\n</ds:EncryptedKey>\n");
                sb.append("\n</ds:EncryptedExchangeKey>\n");
            } else {
                // sinon ok : on met la cle cryptee
                sb.append("\n<ds:EncryptedKey>\n");
                sb.append(new String(Base64.encode(cryptKey)));
                sb.append("\n</ds:EncryptedKey>\n");
                sb.append("\n</ds:EncryptedExchangeKey>\n");
            }
        }
        System.out.println("la taille du fichier de clef est:" + cryptKey.length);
        //String outputKeyFileName = this.NomEnveloppe+".key";
        String outputKeyFile = pathsymkey;// pathEnveloppe + File.separator + enveloppe +".key.p7m";
        try {
            FileOutputStream outkCF = new FileOutputStream(outputKeyFile);
            String contenuKeyFile = sb.toString();
            byte[] towrite = contenuKeyFile.getBytes();
            outkCF.write(towrite);
            outkCF.close();
            return raw;
        } catch (Exception exc) {
            System.out.println("Exception lors de l'ecriture du fichier de cle :" + exc);
        }
        return null;
    }

    public static final String CRYPT_TYPE = "DESede";

    public File cryptFileWithSymKey(byte[] rawkey, File zipToCrypt, String keyFile){
        byte[] buf = new byte[1024];
        String cryptedZipfileName = Configuration.activFolder + File.separator + zipToCrypt.getName() + ".crypt";

        // initialisation du moteur crypto
        // -> note l'algo 3DES est le plus commun sur les JVM dispos
        SecretKeySpec skeySpec = new SecretKeySpec(rawkey, CRYPT_TYPE);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(CRYPT_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            // a  present on initialise l'entree et la sortie
            FileOutputStream outCryptedFile = new FileOutputStream(cryptedZipfileName);
            CipherOutputStream cos = new CipherOutputStream(outCryptedFile, cipher);
            int bytesRead;
            InputStream inputZip2crypt = new FileInputStream(zipToCrypt);
            while((bytesRead = inputZip2crypt.read(buf)) != -1) {
                cos.write(buf, 0, bytesRead);
            }
            cos.close();
            outCryptedFile.close();
            java.util.Arrays.fill(buf, (byte) 0);
            buf = new byte[1024];

            File enveloppe = new File(Configuration.activFolder + File.separator + "ENVELOPPE.zip");
            FileOutputStream outfinalfile = new FileOutputStream(enveloppe);
            ZipOutputStream out = new ZipOutputStream(outfinalfile);


            FileInputStream in = new FileInputStream(keyFile);
            File kfile = new File(keyFile);
            out.putNextEntry(new ZipEntry(kfile.getName()));
            //Transfer bytes from the file to the ZIP file
            int len;
            while ( (len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Complete the entry
            out.closeEntry();
            in.close();
            // suivant : le fichier crypte
            in = new FileInputStream(cryptedZipfileName);
            File cryptedZipfile = new File(cryptedZipfileName);
            out.putNextEntry(new ZipEntry(cryptedZipfile.getName()));
            //Transfer bytes from the file to the ZIP file
            while ( (len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Complete the entry
            out.closeEntry();
            in.close();
            // on ferme le zip
            out.close();
            outfinalfile.close();
            return enveloppe;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Generate a secret TripleDES encryption/decryption key */
    public SecretKey generateKey() throws NoSuchAlgorithmException {
        // Get a key generator for Triple DES (a.k.a DESede)
        KeyGenerator keygen = KeyGenerator.getInstance("DESede");
        keygen.init(168);
        // Use it to generate a key
        return keygen.generateKey();
    }

    public static CryptOkMarchesService getInstance() {
        if (null == instance) {
            getUniqueInstance__();
        }
        return instance;
    }
    synchronized private static void getUniqueInstance__() {
        instance =  new CryptOkMarchesService();
    }

    private static CryptOkMarchesService instance;
}

