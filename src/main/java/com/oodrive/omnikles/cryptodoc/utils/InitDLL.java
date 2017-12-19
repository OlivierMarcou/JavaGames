package com.oodrive.omnikles.cryptodoc.utils;

import com.xdematerialisation.crypto.mscrypto.MsCryptoProvider;
import org.openoces.opensign.wrappers.microsoftcryptoapi.MicrosoftCryptoApi;

import java.io.File;
import java.lang.reflect.Field;


public class InitDLL  {
    public MicrosoftCryptoApi mscapiDLL = new MicrosoftCryptoApi();
    public MsCryptoProvider mscproviderDLL = new MsCryptoProvider();
    static {
        File file = new File("resources");
        String absolutePath = file.getAbsolutePath().replace("resources" , "");
        File dir = new File(  absolutePath + File.separatorChar);
        addToJavaLibraryPath( dir);
        System.out.println("path " + System.getProperty("java.library.path"));
       // System.load(dir.getAbsolutePath() + "\\MicrosoftCryptoApi_0_3.dll");
        System.loadLibrary("MicrosoftCryptoApi_0_3");
        System.loadLibrary("MsCryptoProvider_0_2");
    }

    public static void addToJavaLibraryPath(File dir) {
        final String LIBRARY_PATH = "java.library.path";
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + " is not a directory.");
        }
        String javaLibraryPath = System.getProperty(LIBRARY_PATH);
        System.setProperty(LIBRARY_PATH, javaLibraryPath + File.pathSeparatorChar + dir.getAbsolutePath());

        resetJavaLibraryPath();
    }

    public static void resetJavaLibraryPath() {
        synchronized(Runtime.getRuntime()) {
            try {
                Field field = ClassLoader.class.getDeclaredField("usr_paths");
                field.setAccessible(true);
                field.set(null, null);

                field = ClassLoader.class.getDeclaredField("sys_paths");
                field.setAccessible(true);
                field.set(null, null);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
