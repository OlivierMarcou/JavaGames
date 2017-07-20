package com.oodrive.omnikles.cryptodoc.service;

import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.utils.MsCryptoProvider;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallFunctionMapper;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public class CryptKey {

    private MsCryptoProvider capi = null;

    public CryptKey(){
        System.setProperty("jna.library.path","/home/olivier/workspace/cryptoDoc/libs/MicrosoftCryptoApi_0_3.dll");
        HashMap<String, StdCallFunctionMapper> optionMap = new HashMap<String,    StdCallFunctionMapper>();
        StdCallFunctionMapper myMapper = new StdCallFunctionMapper();
        optionMap.put(Library.OPTION_FUNCTION_MAPPER, myMapper);
        capi = (MsCryptoProvider) Native.loadLibrary("MicrosoftCryptoApi_0_3", MsCryptoProvider.class, optionMap);
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
                cryptKey = capi.cryptMessage(raw, certificate.getCertificate().getEncoded());
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
//        System.load( "libs/MicrosoftCryptoApi_0_3.dll");
//        DLL INSTANCE = (DLL) Native.loadLibrary("libs/MicrosoftCryptoApi_0_3.dll", DLL.class);
//        MsCryptoProvider capi = new MsCryptoProvider();
        byte[] data = capi.cryptMessage(toBeCrypted, certificate);
        if (data == null) {
            int capiError = capi.getLastErrorCode();
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

