package com.oodrive.omnikles.cryptodoc.service;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.CertificateInformations;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;
import com.oodrive.omnikles.cryptodoc.utils.CertificatesUtils;
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
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
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

    private AnimatedProgressBar progressBar = null;

    public AnimatedProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(AnimatedProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    private ZipService zs = ZipService.getInstance();

    private int jobNumber = 0;
    private int maxPercent = 100 ;

    public int getMaxPercent() {
        return maxPercent;
    }

    public void setMaxPercent(int maxPercent) {
        this.maxPercent = maxPercent;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public File cryptedByCertificates(File depositZipFile, List<String> certificats) throws IOException, InvalidKeySpecException {
        if(depositZipFile.exists()) {
            List<CertificateInformations> certs = CertificatesUtils.getCertificatesX509(certificats);
            System.out.println("utils");

            List<File> contentZip = new ArrayList<>();

            try {
                contentZip.add(createKeyFile(certs));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                contentZip.add(encryptFileWithSecretKey(depositZipFile));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            File enveloppeZip = new File(Configuration.activFolder + File.separatorChar + Configuration.FILENAME_ZIP+Configuration.parameters.get("ids")+".zip");
            zs.setProgressBar(progressBar);
            zs.setJobNumber(2);
            zs.setMaxPercent(25);
            zs.addFilesToNewZip(enveloppeZip, contentZip);
            return enveloppeZip;
        }else{
            throw new FileNotFoundException("fichier introuvable : "+ depositZipFile.getAbsolutePath());
        }
    }

    public List<KeyPair> getInstalledCertificates(){
        return CertificatesUtils.getInstalledCertificates();
    }

    public KeyPair getKeyPairWithPrivateKey(String alias, String password) throws Exception {
        return CertificatesUtils.getKeyPairWithAlias(alias, password);
    }

    public List<KeyPair> getKeyPairList( char[] password, File p12Filename) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return CertificatesUtils.getKeyPairList(p12Filename, password);
    }

    private void generateAES256Key(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        secret = new SecretKeySpec(tmp.getEncoded(), Configuration.CRYPTED_KEY_ALGORITHME);
    }

    public File encryptFileWithSecretKey(File file)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IOException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(Configuration.CRYPTED_KEY_ALGORITHME);
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        FileInputStream fin1 = new FileInputStream(file);
        File cryptedFile = new File(Configuration.activFolder
                + File.separatorChar
                + Configuration.FILENAME_CRYPTED_ZIP);
        FileOutputStream fout = new FileOutputStream(cryptedFile);

        byte[] block = new byte[2048];
        int i;
        long size = 0 ;
        int percentMem = -1;
        long fileSize = file.length()/1024;
        while ((fin1.read(block)) != -1) {
            byte[] inputfile= cipher.doFinal(block);
            fout.write(inputfile);
            if(progressBar != null){
                size += block.length/1024;
                int percent = maxPercent;
                if(fileSize > 0) {
                    percent = Math.round((size * maxPercent) / fileSize);
                }
                if(percentMem != percent){
                    progressBar.setActualIcon(percent + (maxPercent*jobNumber));
                    percentMem = percent;
                    progressBar.setText(CryptoDoc.textProperties.getProperty("depot.page4.sending") +
                              (percent + (maxPercent*jobNumber)) + "%");
                }
            }
        }
        return cryptedFile;
    }

    public byte[] decodeSecretKeyByCertificate(byte[] data, KeyPair keyPair) throws CertificateEncodingException {
        byte[] encryptedSecretKey = null;
        try {
            String dataString = new String(data);
            JSONObject arr = new JSONObject(dataString);
            System.out.println("Key File data : "+ dataString);
            JSONObject certificatsSecretKeys = arr.getJSONObject("data");

            System.out.println("JSON certificatsSecretKeys : "+certificatsSecretKeys);

            for (Iterator it = certificatsSecretKeys.keys(); it.hasNext(); ) {
                CertificateInformations certificateInformations = new CertificateInformations((String)it.next());
                if(certificateInformations != null && keyPair.getCertificate() != null
                        && certificateInformations.getX509Certificate().hashCode() == keyPair.getCertificate().hashCode()){
                    System.out.println("IF keyPair == " + keyPair.toString());
                    String encryptedSecretKeyB64 = (String)certificatsSecretKeys.get(certificateInformations.getB64Certificate());
                    System.out.println("Secret : "+encryptedSecretKeyB64);
                    BASE64Decoder decoder = new BASE64Decoder();
                    encryptedSecretKey = decoder.decodeBuffer(encryptedSecretKeyB64);
                    System.out.println("encryptedSecretKey length  == " + encryptedSecretKey.length);
                }
            }
        } catch (JSONException | NullPointerException | IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("CertificatesUtils.provider " + CertificatesUtils.provider.getName() + " " + CertificatesUtils.provider.getInfo());
            Cipher dcipher = Cipher.getInstance(Configuration.CIPHER_ALGORITHME, CertificatesUtils.provider);
            dcipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivateKey());
            System.out.println( "PK " + keyPair.getPrivateKey());
            byte[] secretKey = dcipher.doFinal(encryptedSecretKey);
            return secretKey;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getAllCryptedKey(File fileEncryptKey){
        List<String> encryptedKeys = new ArrayList<>();

        String content = null;
        try {
            content = FileUtils.readFileToString(fileEncryptKey, Configuration.JSON_ENCODING);
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
            SecretKey secretKey = new SecretKeySpec(secret, Configuration.CRYPTED_KEY_ALGORITHME);
            System.out.println("SecretKey : " + secretKey.getEncoded());
            System.out.println("CRYPTED_KEY_ALGORITHME : " + Configuration.CRYPTED_KEY_ALGORITHME);
            Cipher cipher = Cipher.getInstance(Configuration.CRYPTED_KEY_ALGORITHME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            System.out.println("Cipher init ok.");
            FileInputStream inputStream = new FileInputStream(encryptFile);
            System.out.println("Encrypted file length" + encryptFile.length());

            CipherOutputStream out = new CipherOutputStream(new FileOutputStream(decryptedFile), cipher);
            byte[] buffer = new byte[2048];
            int count;
            System.out.println("Send inputBytes to cipher ...");
            while ((count = inputStream.read(buffer)) > 0)
            {
                out.write(buffer, 0, count);
            }

            out.close();
            inputStream.close();
            System.out.println("decryptedFile : " + decryptedFile);

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException
                |  IOException ex) {
            throw new Exception("Error encrypting/decrypting file", ex);
        }
    }

    public File createKeyFile(List<CertificateInformations> certificats) throws IOException, InvalidKeySpecException {
        BASE64Encoder encoder = new BASE64Encoder();
        File jsonKeyFile = new File(Configuration.FILENAME_CRYPTED_KEYS);
        jsonKeyFile.delete();
        HashMap<String, String> dualKeys = new HashMap<>();
        JSONObject json = new JSONObject();
        for(CertificateInformations certificateInformations: certificats){
            //on crypte la secret key avec chaque certificat
            // ensuite on la stocke dans un fichier nommé :
            byte[] encryptedSecretKey = encrypt(secret.getEncoded(), certificateInformations.getX509Certificate());
            if(encryptedSecretKey != null){
                dualKeys.put( certificateInformations.getB64Certificate(), encoder.encode(encryptedSecretKey));
            }
        }
        if(dualKeys.size() > 0) {
            try {
                json.put("data", dualKeys);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FileUtils.writeStringToFile(jsonKeyFile, json.toString(), Configuration.JSON_ENCODING, true);
            return jsonKeyFile;
        }else{
            throw new InvalidKeySpecException("Aucun certificat d'acheteur valide n'a été trouvé !");
        }
    }

    public byte[] encrypt(byte[] data, X509Certificate x509Certificate) {
        try {
            Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, Boolean.FALSE);
        } catch (Exception ex) {
        }
            // La variable cert correspond au certificat du destinataire
            // La clé publique de ce certificat servira à chiffrer la clé symétrique
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(Configuration.CIPHER_ALGORITHME);
            cipher.init(Cipher.ENCRYPT_MODE, x509Certificate.getPublicKey());
            // Encrypt the message
            return cipher.doFinal(data);
        } catch (InvalidKeyException e) {
            System.out.println("Le certificat : "+x509Certificate.getSubjectDN() +" \n n'est pas conçu pour crypter un fichier");
        }  catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AESService getInstance() {
        if (null == instance) {
            getUniqueInstance__();
        }
        return instance;
    }
    synchronized private static void getUniqueInstance__() {
        instance =  new AESService();
    }

    private AESService() {
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

    private static AESService instance;

}
