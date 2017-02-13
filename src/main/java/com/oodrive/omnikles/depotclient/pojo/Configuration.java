package com.oodrive.omnikles.depotclient.pojo;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by olivier on 24/01/17.
 */
public class Configuration {

    public static final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
    public static final boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");
    public static boolean debug = false;
    public static HashMap<String, String> parameters = new HashMap<>();
    public static String activFolder;
    public static long totalSizeFiles = 0;

    public static final String WINDOWS_KEYSTORE = "Windows-MY";
    public static final String FILENAME_ZIP = "enveloppe.zip";
    public static final String FILENAME_CRYPTED_ZIP = "enveloppe_zip.crypt";
    public static final String FILENAME_DECRYPTED_ZIP = "enveloppe_decrypte.zip";
    public static final String FILENAME_CRYPTED_KEYS = "enveloppe.key.p7m";
    public static final String JSON_ENCODING = "UTF-8";
    public static final String CIPHER_ALGORITHME = "RSA";
    public static final String CRYPTED_KEY_ALGORITHME = "AES";
    public static final String WINDOWS_PROVIDER_KEYSTORE = "SunMSCAPI";
    public static final String PREFIX_DECRYPTED_FILENAME = "fichier_decrypte_";
    public static final void initParameters(String[] args) throws IOException {
        activFolder = System.getProperty("user.home");
        System.out.println(activFolder);
        parameters = new HashMap<>();
        String[] keyValue = new String[2];
            for (String parameter: args ){
            int indexEqual = parameter.trim().indexOf("=");
            keyValue[0] = parameter.substring(0, indexEqual);
            keyValue[0].replaceFirst("-","");
            keyValue[1] = parameter.substring(indexEqual+1);
            System.out.println(keyValue[0] + " " + keyValue[1]);
            parameters.put(keyValue[0], keyValue[1]);
        }
        debug = Boolean.parseBoolean(parameters.get("debug"));

    }
}
