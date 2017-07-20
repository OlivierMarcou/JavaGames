package com.oodrive.omnikles.cryptodoc.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by xypho on 20/07/2017.
 */
public interface MsProvider extends Library {

    MsProvider INSTANCE = (MsProvider) Native.loadLibrary( "MicrosoftCryptoApi_0_3", MsProvider.class);
    // it's possible to check the platform on which program runs, for example purposes we assume that there's a linux port of the library (it's not attached to the downloadable project)

    void hello();
    byte[][] getCertificatesInSystemStore(String storeName);
    byte[] signMessage(byte[] toBeSigned, byte[] certificate);
    // FIXME il faudrait pouvoir preciser un tableau de certificats dans le cas de plusieurs destinataires
    byte[] cryptMessage(byte[] toBeCrypted, byte[] certificate);
    byte[] decryptMessage(byte[] crypted, byte[] certificate);

    int getCertificateVersion(byte[] certificate);
    byte[] digest(byte[] data, String algorithm);
    int getLastErrorCode();
    int getMajorVersion();
    int getMinorVersion();
    int getKeyUsage(byte[] certificate);

    Date getNotBeforeDate(byte[] certificate) ;
    Date getNotAfterDate(byte[] certificate) ;
    BigInteger getSerialNumberBigInteger(byte[] certificate) ;

    String getSubjectDnString(byte[] certificate);

    String getIssuerDnString(byte[] certificate) ;


}
