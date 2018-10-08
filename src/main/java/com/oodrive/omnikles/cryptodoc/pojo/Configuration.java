package com.oodrive.omnikles.cryptodoc.pojo;

import com.oodrive.omnikles.cryptodoc.utils.Logs;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Created by olivier on 24/01/17.
 */
public class Configuration {

    private static final String[] nameIdsTender = new String[]{"buyerId","lotId","tenderId","phaseId","publicationId","supplierId","documentId"};
    private static final String[] nameIdsMarches = new String[]{"buyerId","lotId","timestamp","type"};

    public static final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
    public static final boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");
    public static boolean debug = false;
    public static boolean tests = false;
    public static HashMap<String, String> parameters = new HashMap<>();
    public static String activFolder;
    public static String destinationFolderPath = null;
    public static long totalSizeFiles = 0;
    public static String version = null;

    public static final String WINDOWS_KEYSTORE = "Windows-MY";
    public static final String FILENAME_ZIP = "enveloppe";
    public static final String FILENAME_CRYPTED_ZIP = "enveloppe_zip.crypt";
    public static final String FILENAME_DECRYPTED_ZIP = "enveloppe_decrypte.zip";
    public static final String FILENAME_CRYPTED_KEYS = "enveloppe.key.p7m";
    public static final String FILENAME_CRYPTED_KEYS_MARCHES = "ENVELOPPE.key.p7m";
    public static final String JSON_ENCODING = "UTF-8";
    public static final String CIPHER_ALGORITHME = "RSA/ECB/PKCS1Padding";
    public static final String CIPHER_KEY_ALGORITHME_MARCHES = "DESede";
    public static final String CRYPTED_KEY_ALGORITHME = "AES";
    public static boolean isOkMarches = false;

    private static BASE64Decoder decode = new BASE64Decoder();

    public static ProxyConfig proxy;

    public static final void initParameters(String[] args) throws IOException {
        activFolder = System.getProperty("user.home");
        Logs.sp("Activ folder : "+activFolder);
        parameters = new HashMap<>();
        String[] keyValue = new String[2];
        for (String parameter: args ){
            int indexEqual = parameter.trim().indexOf("=");
            keyValue[0] = parameter.substring(0, indexEqual);
            keyValue[0].replaceFirst("-","");
            keyValue[1] = parameter.substring(indexEqual+1);
            if(keyValue[0].equals("titleProcedure") || keyValue[0].equals("organismName") || keyValue[0].equals("liblot"))
                keyValue[1] = new String(decode.decodeBuffer(keyValue[1]), Charset.forName("UTF-8"));
            Logs.sp(keyValue[0] + " " + keyValue[1]);
            if(keyValue[0].toLowerCase().equals("activfolder")) {
                activFolder = keyValue[1];
            }else {
                parameters.put(keyValue[0], keyValue[1]);
            }
        }
        debug = Boolean.parseBoolean(parameters.get("debug"));
        tests = Boolean.parseBoolean(parameters.get("src/main/test"));
    }


    public static HashMap<String, Long> getIdsFile(String filename) throws NumberFormatException{
        String line = filename.toLowerCase().substring(filename.indexOf("_")+1,filename.lastIndexOf("."));
        String[] idsStr = line.split("_");
        HashMap<String, Long> ids = new HashMap<>();
        String[] nameIds = nameIdsTender;
        if(Configuration.isOkMarches)
            nameIds = nameIdsMarches;
        for(int i =0; i < idsStr.length; i++){
            if(idsStr[i] != null){
                try {
                    long id = Long.parseLong(idsStr[i]);
                    ids.put(nameIds[i], id);
                }catch(NumberFormatException ex){}
            }
        }
        return ids;
    }

}
