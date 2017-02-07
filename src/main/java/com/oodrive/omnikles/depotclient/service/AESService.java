package com.oodrive.omnikles.depotclient.service;

import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.utils.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by olivier on 14/11/16.
 */
public class AESService {

    public SecretKey getSecret() {
        return secret;
    }

    public void setSecret(SecretKey secret) {
        this.secret = secret;
    }

    private SecretKey secret = null;


    public AESService(){
        char[] password = new char[10];
        for (int i = 0; i<10; i++){
            long codeAscii = Math.round( 33 + (Math.random() * (127 - 33)));
           password[i] = ((char) codeAscii);
        }
        byte[] salt = new byte[] {1,5,5,6,6};
        try {
            generateAES256Key(password, salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private void generateAES256Key(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        secret = new SecretKeySpec(tmp.getEncoded(), CryptoDocConfiguration.CRYPTED_KEY_ALGORITHME);
    }

    //s'appelerai à terme : encryptFilesWithSecretKey(File[] files, File zipFile)
    public void encryptFileWithSecretKey(File file, File zipFile)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IOException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(CryptoDocConfiguration.CRYPTED_KEY_ALGORITHME);
        cipher.init(Cipher.ENCRYPT_MODE, secret);

        //TODO : ajouter ici une liste de fichier à crypter, les zipper et ensuite crypter le zip
        // une fois le zip crypté on l'ajoute au zip de l'enveloppe.

        FileInputStream fin1 = new FileInputStream(file);
        File cryptedFile = new File(CryptoDocConfiguration.activFolder
                + File.separatorChar
                + CryptoDocConfiguration.FILENAME_CRYPTED_ZIP);
        FileOutputStream fout = new FileOutputStream(cryptedFile);

        byte[] block = new byte[(int) file.length()];
        int i;
        while ((i = fin1.read(block)) != -1) {
            byte[] inputfile= cipher.doFinal(block);
            fout.write(inputfile);
        }
        ZipUtils.addFileToZip(cryptedFile, zipFile);
    }

    public byte[] decodeSecretKeyByCertificat(byte[] data, KeyPair keyPair) {
        byte[] encryptedSecretKey = null;
        try {
            JSONObject arr = new JSONObject(new String(data));
            JSONObject certificatsSecretKeys = arr.getJSONObject("data");

            for (Iterator it = certificatsSecretKeys.keys(); it.hasNext(); ) {
                String certificate = (String)it.next();
                if(certificate != null && keyPair.getX509CertificateB64() != null
                        && certificate.equals(keyPair.getX509CertificateB64())){
                    String encryptedSecretKeyB64 = (String)certificatsSecretKeys.get(certificate);
                    BASE64Decoder decoder = new BASE64Decoder();
                    encryptedSecretKey = decoder.decodeBuffer(encryptedSecretKeyB64);
                }
            }
        } catch (JSONException | NullPointerException | IOException e) {
            e.printStackTrace();
        }
        try {
            Cipher dcipher = Cipher.getInstance(CryptoDocConfiguration.CIPHER_ALGORITHME, CryptoDocConfiguration.WINDOWS_PROVIDER_KEYSTORE);
            dcipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivateKey());
            return dcipher.doFinal(encryptedSecretKey);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getAllCryptedKey(File fileEncryptKey){
        List<String> encryptedKeys = new ArrayList<>();

        String content = null;
        try {
            content = FileUtils.readFileToString(fileEncryptKey, CryptoDocConfiguration.JSON_ENCODING);
            try {
                return new JSONObject(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void decryptFileWithSecretKey(File encryptFile, File decryptedFile, byte[] secret)
            throws Exception {
        System.out.println("Methode decryptFileWithSecretKey...");
        System.out.println("EncryptFile existe ?" + encryptFile.exists());
        try {
            SecretKey secretKey = new SecretKeySpec(secret, CryptoDocConfiguration.CRYPTED_KEY_ALGORITHME);
            System.out.println("SecretKey : " + secretKey.getEncoded());
            System.out.println("CRYPTED_KEY_ALGORITHME : " + CryptoDocConfiguration.CRYPTED_KEY_ALGORITHME);
            Cipher cipher = Cipher.getInstance(CryptoDocConfiguration.CRYPTED_KEY_ALGORITHME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            System.out.println("Cipher init ok.");
            FileInputStream inputStream = new FileInputStream(encryptFile);
            System.out.println("Encrypted file length" + encryptFile.length());
            byte[] inputBytes = new byte[(int) encryptFile.length()];
            inputStream.read(inputBytes);

            System.out.println("inputBytes : " + inputBytes);
            byte[] outputBytes = cipher.doFinal(inputBytes);

            System.out.println("decryptedFile : " + decryptedFile);
            FileOutputStream outputStream = new FileOutputStream(decryptedFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new Exception("Error encrypting/decrypting file", ex);
        }
    }

    public void addFileKeyToZip(File fileSecretKey, File destinationZip, boolean isNew){
        try{
            System.out.println("Add secret file to zip ... Start");
            if(isNew)FileUtils.deleteQuietly(destinationZip);
            ZipUtils.addFileToZip(fileSecretKey, destinationZip);
            System.out.println("Add secret file to zip ... Finish");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public File zipKeyFile(List<KeyPair> certificats, String zipDestinationName) throws IOException {
        BASE64Encoder encoder = new BASE64Encoder();
        File enveloppe = new File(CryptoDocConfiguration.FILENAME_CRYPTED_KEYS);
        enveloppe.delete();
        HashMap<String, String> dualKeys = new HashMap<>();
        JSONObject json = new JSONObject();
        for(KeyPair keyPair: certificats){
            //on crypte la secret key avec chaque certificat
            // ensuite on la stocke dans un fichier nommé :
            byte[] encryptedSecretKey = encrypt(secret.getEncoded(), keyPair.getCertificate());
            if(encryptedSecretKey != null){
                dualKeys.put( keyPair.getX509CertificateB64(), encoder.encode(encryptedSecretKey));
            }
        }

        try {
            json.put("data",dualKeys);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        FileUtils.writeStringToFile(enveloppe, json.toString(), CryptoDocConfiguration.JSON_ENCODING, true);
        addFileKeyToZip(enveloppe, new File(zipDestinationName), true);
        return new File (zipDestinationName);
    }

    public String decryptMessage(byte[] key, KeyPair keyPair, String filename) {
        byte [] decripted  = null;
        try {
            if (keyPair.getPrivateKey() != null) {
                Cipher cipher = Cipher.getInstance(CryptoDocConfiguration.CIPHER_ALGORITHME,
                        Security.getProvider(CryptoDocConfiguration.WINDOWS_PROVIDER_KEYSTORE));
                cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivateKey());
                cipher.update(key);
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
            envfos = new FileOutputStream(CryptoDocConfiguration.activFolder
                    + File.separatorChar + CryptoDocConfiguration.PREFIX_DECRYPTED_FILENAME + filename );
            envfos.write(decripted);
            envfos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "ok";
    }


    public byte[] encrypt(byte[] data, X509Certificate x509Certificate) {
        try {
            Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, Boolean.FALSE);
        } catch (Exception ex) {
        }
        String resultat = null;
        try {
            // La variable cert correspond au certificat du destinataire
            // La clé publique de ce certificat servira à chiffrer la clé symétrique
            Cipher cipher = Cipher.getInstance(CryptoDocConfiguration.CIPHER_ALGORITHME,
                    CryptoDocConfiguration.WINDOWS_PROVIDER_KEYSTORE);
            cipher.init(Cipher.ENCRYPT_MODE, x509Certificate);
            // Encrypt the message
            return cipher.doFinal(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptMessage(File file, KeyPair keyPair) {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
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
                    Cipher cipher = Cipher.getInstance(CryptoDocConfiguration.CIPHER_ALGORITHME,
                            Security.getProvider(CryptoDocConfiguration.WINDOWS_PROVIDER_KEYSTORE));
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
                envfos = new FileOutputStream(CryptoDocConfiguration.activFolder
                        + File.separatorChar + CryptoDocConfiguration.PREFIX_DECRYPTED_FILENAME + file.getName());
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


}
