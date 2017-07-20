package com.oodrive.omnikles.cryptodoc.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Date;


public class MsCryptoProvider  {

    // load DLL that contains static method
    static {
        File dir = new File(  "C:"+ File.separatorChar + "Users"+ File.separatorChar +
                "xypho"+ File.separatorChar +"Documents"+ File.separatorChar +
                "cryptoDoc"+ File.separatorChar +"src"+ File.separatorChar +
                "main"+ File.separatorChar +"resources"+ File.separatorChar);
        addToJavaLibraryPath( dir);
        System.out.println("path " + System.getProperty("java.library.path"));
       // System.load(dir.getAbsolutePath() + "\\MicrosoftCryptoApi_0_3.dll");
        System.loadLibrary("MicrosoftCryptoApi_0_3");
    }
    public interface Kernel32 extends Library {
        // FREQUENCY is expressed in hertz and ranges from 37 to 32767
        // DURATION is expressed in milliseconds
        public boolean Beep(int FREQUENCY, int DURATION);
        public void Sleep(int DURATION);
    }

  public MsCryptoProvider() {
        Kernel32 lib = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
        lib.Beep(698, 500);

      MsProvider sdll = MsProvider.INSTANCE;
      sdll.hello();  // call of void function
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

    /**
     * Supprime le cache du "java.library.path".
     * Cela forcera le classloader à revérifier sa valeur lors du prochaine chargement de librairie.
     *
     * Attention : ceci est spécifique à la JVM de Oracle et pourrait ne pas fonctionner
     * sur une autre JVM...
     */
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

  public static native void hello();
  public static native byte[][] getCertificatesInSystemStore(String storeName);
  public static native byte[] signMessage(byte[] toBeSigned, byte[] certificate);
  // FIXME il faudrait pouvoir preciser un tableau de certificats dans le cas de plusieurs destinataires
  public static native byte[] cryptMessage(byte[] toBeCrypted, byte[] certificate);
  public static native byte[] decryptMessage(byte[] crypted, byte[] certificate);

  private static native String getSubjectDn(byte[] certificate);
  private static native String getIssuerDn(byte[] certificate);
  public static native int getCertificateVersion(byte[] certificate);
  public static native byte[] digest(byte[] data, String algorithm);
  public static native int getLastErrorCode();
  public static native int getMajorVersion();
  public static native int getMinorVersion();
  public static native int getKeyUsage(byte[] certificate);

public Date getNotBeforeDate(byte[] certificate) {
  // attention prendre en compte la conversion long ms -> long java
    return new Date(getNotBefore(certificate)); //FIXME
}
public Date getNotAfterDate(byte[] certificate) {
  // attention prendre en compte la conversion long ms -> long java
  return new Date(getNotAfter(certificate)); // FIXME
}

public BigInteger getSerialNumberBigInteger(byte[] certificate) {
    byte[] serialNumber = getSerialNumber(certificate);

    /* convert from little endian to big endian */
    for ( int i=0; i < ( serialNumber.length/2 ); i++) {
        int lowerIdx = i;
        int upperIdx = serialNumber.length-i-1;
        byte b = serialNumber[lowerIdx];
        serialNumber[lowerIdx] = serialNumber[upperIdx];
        serialNumber[upperIdx] = b;
    }

    return new BigInteger(serialNumber);
}

public String getSubjectDnString(byte[] certificate) {
    String s = getSubjectDn(certificate);
    return s == null ? null : s.substring(0,s.length()-1);
}

public String getIssuerDnString(byte[] certificate) {
    String s = getIssuerDn(certificate);
    return s == null ? null : s.substring(0,s.length()-1);
}

private static native byte[] getSerialNumber(byte[] certificate);

private static native long getNotAfter(byte[] certificate);
private static native long getNotBefore(byte[] certificate);

//  public KeyUsage getIntendedKeyUsage(byte[] certificate) {
//    int i = getKeyUsage(certificate);
//    return new KeyUsage((i & 1) != 0,
//                        (i & 2) != 0,
//                        (i & 4) != 0,
//                        (i & 8) != 0,
//                        (i & 16) != 0,
//                        (i & 64) != 0,
//                        (i & 32) != 0);
//}

}
