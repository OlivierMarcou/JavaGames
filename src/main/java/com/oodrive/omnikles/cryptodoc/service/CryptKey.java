package com.oodrive.omnikles.cryptodoc.service;

import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.utils.InitDLL;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CryptKey {
    InitDLL initDLL = new InitDLL();
    public CryptKey(){
        System.out.println("Version : " + initDLL.mscapiDLL.getMajorVersion());

    }

    public byte[] genereSymKeyFile(String pathsymkey, List<KeyPair> certificates) {
        KeyGenerator kgen = null;
        try {
            kgen = KeyGenerator.getInstance("DESede");
        } catch (NoSuchAlgorithmException ex1) {
	        /* Rajouter la prise en compte du cas : algo non disponible */
            return null;
        }
        kgen.init(168); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        // crypter cette cle symetrique a l'aide de la cle publique
        byte[] cryptKey = raw;
        StringBuffer sb = new StringBuffer();
        BASE64Encoder encode = new BASE64Encoder();
        for (KeyPair certificate : certificates) {

            sb.append("<ds:EncryptedExchangeKey>\n");
            sb.append("\n<ds:X509Certificate>\n");
            sb.append(certificate.getX509CertificateB64());
            sb.append("\n</ds:X509Certificate>\n");
            try {
                cryptKey =  initDLL.mscproviderDLL.cryptMessage(raw, certificate.getCertificate().getEncoded());
            } catch (Exception exc) {
                System.out.println("Erreur lors du chiffrement asymetrique de la cle symetrique");
                exc.printStackTrace();
            }
	          /* ************************************************************** */
	          /* MBT : correction applet du 19/04 : dans le cas de versions  de Win non a jour */
            if ((cryptKey == null) || (cryptKey.length == 0)) {
                // Erreur au niveau du cryptage
                // dans ce cas, on met la cle symetrique en clair ...
                sb.append("\n<ds:EncryptedKey>\n");
                sb.append(encode.encode(raw));
                sb.append("\n</ds:EncryptedKey>\n");
                sb.append("\n</ds:EncryptedExchangeKey>\n");
                System.out.println("Erreur: l'operation de cryptage a echoue - Mettre e jour Windows");
            } else {
                // sinon ok : on met la cle cryptee
                sb.append("\n<ds:EncryptedKey>\n");
                sb.append(encode.encode(cryptKey));
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

    private File cryptFileWithSymKey(byte[] rawkey, File zipfile, String keyFile){
        byte[] buf = new byte[1024];
        String tempzipfile = zipfile.getAbsolutePath()+".temp";

        // initialisation du moteur crypto
        // -> note l'algo 3DES est le plus commun sur les JVM dispos
        SecretKeySpec skeySpec = new SecretKeySpec(rawkey, CRYPT_TYPE);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(CRYPT_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            // a  present on initialise l'entree et la sortie
            FileOutputStream outCryptedFile = new FileOutputStream(tempzipfile);
            CipherOutputStream cos = new CipherOutputStream(outCryptedFile, cipher);
            int bytesRead;
            InputStream inputZip2crypt = new FileInputStream(zipfile);
            while((bytesRead = inputZip2crypt.read(buf)) != -1) {
                cos.write(buf, 0, bytesRead);
            }
            cos.close();
            outCryptedFile.close();
            java.util.Arrays.fill(buf, (byte) 0);
            buf = new byte[1024];

            File finalFile = new File(zipfile.getAbsolutePath()+".zip.crypt");
            FileOutputStream outfinalfile = new FileOutputStream(finalFile);
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
            in = new FileInputStream(tempzipfile);
            File cfile = new File(zipfile.getAbsolutePath()+".crypt");
            out.putNextEntry(new ZipEntry(cfile.getName()));
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
            return finalFile;
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

    public byte[] cryptMessage(byte[] toBeCrypted, byte[] certificate) throws Exception {

        byte[] data = initDLL.mscapiDLL.cryptMessage(toBeCrypted, certificate);
        if (data == null) {
            int capiError = initDLL.mscapiDLL.getLastErrorCode();
            // todo: check errornumber for various conditions and react properly - telling the user when needed etc
            // note that different CSP's can return different codes etc.
            // for now: expect the user to have cancelled
            throw new Exception("error capicom" + capiError);
        }
        if (data == null)
            throw new IOException("cryptMessage was cancelled");
        return data;
    }
}

