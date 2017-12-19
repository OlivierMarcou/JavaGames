package com.xdematerialisation.crypto.mscrypto;

import java.util.Date;
import java.math.BigInteger;



public class MsCryptoProvider {
    public MsCryptoProvider() {
    }

    public native void hello();
    public native byte[][] getCertificatesInSystemStore(String storeName);
    public native byte[] signMessage(byte[] toBeSigned, byte[] certificate);
    // FIXME il faudrait pouvoir preciser un tableau de certificats dans le cas de plusieurs destinataires
    public native byte[] cryptMessage(byte[] toBeCrypted, byte[] certificate);
    public native byte[] decryptMessage(byte[] crypted, byte[] certificate);

    private native String getSubjectDn(byte[] certificate);
    private native String getIssuerDn(byte[] certificate);
    public native int getCertificateVersion(byte[] certificate);
    public native byte[] digest(byte[] data, String algorithm);
    public native int getLastErrorCode();
    public native int getMajorVersion();
    public native int getMinorVersion();
    public native int getKeyUsage(byte[] certificate);

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

    private native byte[] getSerialNumber(byte[] certificate);

    private native long getNotAfter(byte[] certificate);
    private native long getNotBefore(byte[] certificate);
}