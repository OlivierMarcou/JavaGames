package com.oodrive.omnikles.cryptodoc.utils;

import java.io.IOException;

// iF NOT DLL USE THIS ONE
// import com.xdematerialisation.crypto.mscrypto6.MsCryptoProvider;
// ELSE

public class CAPI {
	MsCryptoProvider capi = new MsCryptoProvider();

	
	public byte[] cryptMessage(byte[] toBeCrypted, byte[] certificate) throws Exception{
		byte[] data = capi.cryptMessage(toBeCrypted, certificate);
		if (data==null){
            int capiError = capi.getLastErrorCode();
            // todo: check errornumber for various conditions and react properly - telling the user when needed etc
            // note that different CSP's can return different codes etc.
            // for now: expect the user to have cancelled
             throw new Exception("erro capicom" + capiError);
        }
		if (data==null)
        	throw new IOException("cryptMessage was cancelled");
		return data;
	}
	
	
	public byte[] decryptMessage(byte[] binaryCertificate, byte[] crypted, char[] password) throws Exception{
		System.out.println("dechiffrement  :\n"+ crypted);
        byte[] data = null;
		try {
			data = capi.decryptMessage(crypted, binaryCertificate);
		} catch (Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
		}
        if(data == null){
            data = crypted;
            System.out.println("not crypted ");
        }
        if (data==null){
            int capiError = capi.getLastErrorCode();
			 System.out.println("Erreur MS :\n"+capiError);
			 
            // todo: check errornumber for various conditions and react properly - telling the user when needed etc
            // note that different CSP's can return different codes etc.
            // for now: expect the user to have cancelled
             //throw new UserCancel();
        }
        //if (data==null)
        	//throw new IOException("decryptMessage was cancelled");
        System.out.println("decryptMessage value:\n"+ data);
        System.out.println("Got error code from microsoft capi wrapper: " + capi.getLastErrorCode());
        return data;
	}
	

}
