package org.openoces.opensign.wrappers.microsoftcryptoapi;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by xypho on 20/07/2017.
 */
public class MicrosoftCryptoApi   {

    public native void hello();
    public native byte[][] getCertificatesInSystemStore(String storeName);
    public native byte[] signMessage(byte[] toBeSigned, byte[] certificate);
    // FIXME il faudrait pouvoir preciser un tableau de certificats dans le cas de plusieurs destinataires
    public native  byte[] cryptMessage(byte[] toBeCrypted, byte[] certificate);
    public native byte[] decryptMessage(byte[] crypted, byte[] certificate);

    public native int getCertificateVersion(byte[] certificate);
    public native byte[] digest(byte[] data, String algorithm);
    public native int getLastErrorCode();
    public native int getMajorVersion();
    public native int getMinorVersion();
    public native int getKeyUsage(byte[] certificate);

    public native Date getNotBeforeDate(byte[] certificate) ;
    public native Date getNotAfterDate(byte[] certificate) ;
    public native BigInteger getSerialNumberBigInteger(byte[] certificate) ;

    public native String getSubjectDnString(byte[] certificate);

    public native String getIssuerDnString(byte[] certificate) ;

}
