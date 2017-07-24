package com.oodrive.omnikles.cryptodoc.service;

import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.utils.InitDLL;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import java.util.List;

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

